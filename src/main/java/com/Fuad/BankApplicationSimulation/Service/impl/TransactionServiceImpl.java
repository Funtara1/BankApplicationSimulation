package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.Request.ResponseDTO.TransactionResponse;
import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import com.Fuad.BankApplicationSimulation.Enums.TransactionStatus;
import com.Fuad.BankApplicationSimulation.Enums.TransactionType;
import com.Fuad.BankApplicationSimulation.Repository.AccountRepository;
import com.Fuad.BankApplicationSimulation.Repository.TransactionRepository;
import com.Fuad.BankApplicationSimulation.Service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    @Override
    @Transactional
    public Transaction deposit(Long toAccountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account toAccount = getAccount(toAccountId);

        BigDecimal oldBalance = toAccount.getBalance();
        BigDecimal newBalance = oldBalance.add(amount);

        toAccount.setBalance(newBalance);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setOldBalance(oldBalance);
        transaction.setNewBalance(newBalance);
        transaction.setToAccount(toAccount);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        return transactionRepository.save(transaction);
    }


    @Override
    @Transactional
    public Transaction withdraw(Long fromAccountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        Account fromAccount = getAccount(fromAccountId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        BigDecimal oldBalance = fromAccount.getBalance();
        BigDecimal newBalance = oldBalance.subtract(amount);

        fromAccount.setBalance(newBalance);
        accountRepository.save(fromAccount);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAW)
                .amount(amount)
                .oldBalance(oldBalance)
                .newBalance(newBalance)
                .fromAccount(fromAccount)
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        return transactionRepository.save(transaction);
    }


    @Override
    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("You cannot transfer to the same account");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }


        BigDecimal fromOld = fromAccount.getBalance();
        BigDecimal fromNew = fromOld.subtract(amount);

        BigDecimal toOld = toAccount.getBalance();
        BigDecimal toNew = toOld.add(amount);

        // Обновляем счета
        fromAccount.setBalance(fromNew);
        toAccount.setBalance(toNew);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.MONEY_TRANSFER);
        transaction.setAmount(amount);
        transaction.setOldBalance(fromOld);
        transaction.setNewBalance(fromNew);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        return transactionRepository.save(transaction);
    }


    @Override
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        List<TransactionResponse> responseList = new ArrayList<>();

        for (Transaction t : transactions) {

            TransactionResponse dto = new TransactionResponse();

            dto.setDate(t.getTimestamp());
            dto.setType(t.getTransactionType().name());
            dto.setAmount(t.getAmount());
            dto.setOldBalance(t.getOldBalance());
            dto.setNewBalance(t.getNewBalance());

            // fromAccount может быть null
            if (t.getFromAccount() != null) {
                dto.setFromAccount(t.getFromAccount().getAccountNumber());
            }

            // toAccount может быть null
            if (t.getToAccount() != null) {
                dto.setToAccount(t.getToAccount().getAccountNumber());
            }

            responseList.add(dto);
        }

        return responseList;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }


    private Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
}
