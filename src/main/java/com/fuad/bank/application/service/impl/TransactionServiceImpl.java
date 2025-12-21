package com.fuad.bank.application.service.impl;

import com.fuad.bank.api.dto.TransactionDTO.RequestDTO.TransactionFilterRequest;
import com.fuad.bank.api.dto.TransactionDTO.ResponseDTO.TransactionResponse;
import com.fuad.bank.application.service.TransactionService;
import com.fuad.bank.domain.entity.Account;
import com.fuad.bank.domain.entity.Transaction;
import com.fuad.bank.domain.enums.*;
import com.fuad.bank.domain.exception.InsufficientFundsException;
import com.fuad.bank.domain.exception.InvalidOperationException;
import com.fuad.bank.domain.exception.NotFoundException;
import com.fuad.bank.domain.exception.ValidationException;
import com.fuad.bank.api.mapper.TransactionMapper;
import com.fuad.bank.infrastructure.persistence.repository.AccountRepository;
import com.fuad.bank.infrastructure.persistence.repository.TransactionRepository;
import com.fuad.bank.infrastructure.persistence.specification.TransactionSpecification;
import com.fuad.bank.domain.util.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionAuditService auditService;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction deposit(Long toAccountId, BigDecimal amount) {
        validateAmount(amount);

        Account toAccount = getAccountForUpdate(toAccountId);
        validateAccountCanOperate(toAccount);

        Transaction pending = auditService.createPending(TransactionType.DEPOSIT, amount);
        auditService.attachAccounts(pending.getId(), null, toAccount.getId());

        try {
            BigDecimal oldBalance = toAccount.getBalance();
            BigDecimal newBalance = oldBalance.add(amount);

            toAccount.setBalance(newBalance);
            accountRepository.save(toAccount);

            auditService.markCompletedSingle(pending.getId(), oldBalance, newBalance);
            return getTx(pending.getId());

        } catch (RuntimeException ex) {
            auditService.markFailed(pending.getId(), ex);
            throw ex;
        }
    }

    @Override
    public Transaction withdraw(Long fromAccountId, BigDecimal amount) {
        validateAmount(amount);

        Account fromAccount = getAccountForUpdate(fromAccountId);
        validateAccountCanOperate(fromAccount);

        Transaction pending = auditService.createPending(TransactionType.WITHDRAW, amount);
        auditService.attachAccounts(pending.getId(), fromAccount.getId(), null);

        try {
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(
                        "Insufficient funds",
                        Map.of(
                                "balance", fromAccount.getBalance(),
                                "amount", amount
                        )
                );
            }

            BigDecimal oldBalance = fromAccount.getBalance();
            BigDecimal newBalance = oldBalance.subtract(amount);

            fromAccount.setBalance(newBalance);
            accountRepository.save(fromAccount);

            auditService.markCompletedSingle(pending.getId(), oldBalance, newBalance);
            return getTx(pending.getId());

        } catch (RuntimeException ex) {
            auditService.markFailed(pending.getId(), ex);
            throw ex;
        }
    }

    @Override
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        validateAmount(amount);

        if (fromAccountId.equals(toAccountId)) {
            throw new ValidationException(
                    "Cannot transfer to the same account",
                    Map.of("accountId", fromAccountId)
            );
        }

        Long firstId = Math.min(fromAccountId, toAccountId);
        Long secondId = Math.max(fromAccountId, toAccountId);

        List<Account> accounts =
                accountRepository.findAllByIdForUpdate(List.of(firstId, secondId));

        Account fromAccount = findAccount(accounts, fromAccountId);
        Account toAccount = findAccount(accounts, toAccountId);

        validateAccountCanOperate(fromAccount);
        validateAccountCanOperate(toAccount);

        Transaction pending =
                auditService.createPending(TransactionType.MONEY_TRANSFER, amount);
        auditService.attachAccounts(pending.getId(), fromAccount.getId(), toAccount.getId());

        try {
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(
                        "Insufficient funds",
                        Map.of(
                                "balance", fromAccount.getBalance(),
                                "amount", amount
                        )
                );
            }

            BigDecimal oldFrom = fromAccount.getBalance();
            BigDecimal oldTo = toAccount.getBalance();

            // Naw Exchange
            BigDecimal convertedAmount = CurrencyConverter.convert(
                    amount,
                    fromAccount.getCurrency(),
                    toAccount.getCurrency()
            );

            BigDecimal newFrom = oldFrom.subtract(amount);
            BigDecimal newTo = oldTo.add(convertedAmount);

            fromAccount.setBalance(newFrom);
            toAccount.setBalance(newTo);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            auditService.markCompletedTransfer(
                    pending.getId(), oldFrom, newFrom, oldTo, newTo
            );

            return getTx(pending.getId());

        } catch (RuntimeException ex) {
            auditService.markFailed(pending.getId(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions =
                transactionRepository.findAllByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);

        List<TransactionResponse> responses = new ArrayList<>();
        for (Transaction t : transactions) {
            responses.add(transactionMapper.toResponse(t));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return getTx(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> filter(
            TransactionFilterRequest filter,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Transaction> specification =
                Specification.where(TransactionSpecification.byAccount(filter.getAccountId()))
                        .and(TransactionSpecification.typeEquals(filter.getType()))
                        .and(TransactionSpecification.statusEquals(filter.getStatus()));

        Page<Transaction> transactions =
                transactionRepository.findAll(specification, pageable);

        List<TransactionResponse> responses = new ArrayList<>();
        for (Transaction t : transactions.getContent()) {
            responses.add(transactionMapper.toResponse(t));
        }

        return new PageImpl<>(
                responses,
                pageable,
                transactions.getTotalElements()
        );
    }


    private void validateAccountCanOperate(Account account) {
        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Account is closed",
                    Map.of("accountId", account.getId())
            );
        }

        if (account.getCustomer() == null) {
            throw new InvalidOperationException(
                    "Account has no customer",
                    Map.of("accountId", account.getId())
            );
        }

        if (account.getCustomer().getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException(
                    "Customer is closed",
                    Map.of("customerId", account.getCustomer().getId())
            );
        }
    }

    private Account findAccount(List<Account> accounts, Long id) {
        for (Account account : accounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        throw new NotFoundException("Account not found", Map.of("accountId", id));
    }

    private Transaction getTx(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found", Map.of("transactionId", id)));
    }

    private Account getAccountForUpdate(Long id) {
        return accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Account not found", Map.of("accountId", id)));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero", Map.of("amount", amount));
        }
    }

}
