package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.ResponseDTO.TransactionResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import com.Fuad.BankApplicationSimulation.Enums.*;
import com.Fuad.BankApplicationSimulation.Exception.*;
import com.Fuad.BankApplicationSimulation.Repository.AccountRepository;
import com.Fuad.BankApplicationSimulation.Repository.TransactionRepository;
import com.Fuad.BankApplicationSimulation.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionAuditService auditService;

    @Override
    public Transaction deposit(Long toAccountId, BigDecimal amount) {
        validateAmount(amount);

        Account toAccount = getAccountForUpdate(toAccountId);
        checkCustomerIsActive(toAccount);

        Transaction pending = auditService.createPending(TransactionType.DEPOSIT, amount);
        auditService.attachAccounts(pending.getId(), null, toAccount);

        try {
            if (toAccount.getAccountStatus() == AccountStatus.CLOSED) {
                throw new InvalidOperationException("Account is closed");
            }

            BigDecimal oldBalance = toAccount.getBalance();
            BigDecimal newBalance = oldBalance.add(amount);

            toAccount.setBalance(newBalance);
            accountRepository.save(toAccount);

            auditService.markCompletedDeposit(pending.getId(), oldBalance, newBalance);
            return getTx(pending.getId());

        } catch (RuntimeException ex) {
            auditService.markFailed(pending.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Transaction withdraw(Long fromAccountId, BigDecimal amount) {
        validateAmount(amount);

        Account fromAccount = getAccountForUpdate(fromAccountId);
        checkCustomerIsActive(fromAccount);

        Transaction pending = auditService.createPending(TransactionType.WITHDRAW, amount);
        auditService.attachAccounts(pending.getId(), fromAccount, null);

        try {
            if (fromAccount.getAccountStatus() == AccountStatus.CLOSED) {
                throw new InvalidOperationException("Account is closed");
            }

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }

            BigDecimal oldBalance = fromAccount.getBalance();
            BigDecimal newBalance = oldBalance.subtract(amount);

            fromAccount.setBalance(newBalance);
            accountRepository.save(fromAccount);

            auditService.markCompletedWithdraw(pending.getId(), oldBalance, newBalance);
            return getTx(pending.getId());

        } catch (RuntimeException ex) {
            auditService.markFailed(pending.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        validateAmount(amount);

        if (fromAccountId.equals(toAccountId)) {
            throw new BusinessException("Cannot transfer to the same account");
        }

        Long firstId = Math.min(fromAccountId, toAccountId);
        Long secondId = Math.max(fromAccountId, toAccountId);

        List<Account> accounts = accountRepository.findAllByIdForUpdate(List.of(firstId, secondId));

        Account fromAccount = null;
        Account toAccount = null;

        for (Account acc : accounts) {
            if (acc.getId().equals(fromAccountId)) fromAccount = acc;
            if (acc.getId().equals(toAccountId)) toAccount = acc;
        }

        if (fromAccount == null || toAccount == null) {
            throw new NotFoundException("Account not found");
        }

        checkCustomerIsActive(fromAccount);
        checkCustomerIsActive(toAccount);

        Transaction pending = auditService.createPending(TransactionType.MONEY_TRANSFER, amount);
        auditService.attachAccounts(pending.getId(), fromAccount, toAccount);

        try {
            if (fromAccount.getAccountStatus() == AccountStatus.CLOSED ||
                    toAccount.getAccountStatus() == AccountStatus.CLOSED) {
                throw new InvalidOperationException("Account is closed");
            }

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }

            BigDecimal oldFrom = fromAccount.getBalance();
            BigDecimal oldTo = toAccount.getBalance();

            BigDecimal newFrom = oldFrom.subtract(amount);
            BigDecimal newTo = oldTo.add(amount);

            fromAccount.setBalance(newFrom);
            toAccount.setBalance(newTo);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            auditService.markCompletedTransfer(pending.getId(), oldFrom, newFrom, oldTo, newTo);
            return getTx(pending.getId());

        } catch (RuntimeException ex) {
            auditService.markFailed(pending.getId(), ex.getMessage());
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
            responses.add(toResponse(t));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return getTx(id);
    }

    private void checkCustomerIsActive(Account account) {
        if (account.getCustomer() == null) {
            throw new InvalidOperationException("Account has no customer");
        }
        if (account.getCustomer().getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidOperationException("Customer is closed");
        }
    }

    private Transaction getTx(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    private Account getAccountForUpdate(Long id) {
        return accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than zero");
        }
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .date(t.getTimestamp())
                .type(t.getTransactionType().getDescription())
                .amount(t.getAmount())
                .oldBalance(t.getOldBalance())
                .newBalance(t.getNewBalance())
                .oldToBalance(t.getOldToBalance())
                .newToBalance(t.getNewToBalance())
                .fromAccount(t.getFromAccount() != null ? t.getFromAccount().getAccountNumber() : null)
                .toAccount(t.getToAccount() != null ? t.getToAccount().getAccountNumber() : null)
                .status(t.getTransactionStatus())
                .reason(t.getErrorMessage())
                .build();
    }
}
