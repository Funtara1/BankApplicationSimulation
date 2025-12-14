package com.Fuad.BankApplicationSimulation.Service.impl;

import com.Fuad.BankApplicationSimulation.Entity.Account;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import com.Fuad.BankApplicationSimulation.Enums.TransactionStatus;
import com.Fuad.BankApplicationSimulation.Enums.TransactionType;
import com.Fuad.BankApplicationSimulation.Exception.NotFoundException;
import com.Fuad.BankApplicationSimulation.Repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionAuditService {

    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction createPending(TransactionType type, BigDecimal amount) {
        Transaction tx = new Transaction();
        tx.setTransactionType(type);
        tx.setAmount(amount);
        tx.setTransactionStatus(TransactionStatus.PENDING);
        return transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void attachAccounts(Long txId, Account fromAccount, Account toAccount) {
        Transaction tx = getTx(txId);

        if (fromAccount != null) {
            tx.setFromAccount(fromAccount);
        }
        if (toAccount != null) {
            tx.setToAccount(toAccount);
        }

        transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompletedDeposit(Long txId, BigDecimal oldBalance, BigDecimal newBalance) {
        Transaction tx = getTx(txId);
        tx.setOldBalance(oldBalance);
        tx.setNewBalance(newBalance);
        tx.setOldToBalance(null);
        tx.setNewToBalance(null);
        tx.setErrorMessage(null);
        tx.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompletedWithdraw(Long txId, BigDecimal oldBalance, BigDecimal newBalance) {
        Transaction tx = getTx(txId);
        tx.setOldBalance(oldBalance);
        tx.setNewBalance(newBalance);
        tx.setOldToBalance(null);
        tx.setNewToBalance(null);
        tx.setErrorMessage(null);
        tx.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompletedTransfer(Long txId,
                                      BigDecimal oldFrom, BigDecimal newFrom,
                                      BigDecimal oldTo, BigDecimal newTo) {

        Transaction tx = getTx(txId);
        tx.setOldBalance(oldFrom);
        tx.setNewBalance(newFrom);
        tx.setOldToBalance(oldTo);
        tx.setNewToBalance(newTo);
        tx.setErrorMessage(null);
        tx.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long txId, String reason) {
        Transaction tx = getTx(txId);
        tx.setTransactionStatus(TransactionStatus.FAILED);
        tx.setErrorMessage(shortReason(reason));
        transactionRepository.save(tx);
    }

    private String shortReason(String reason) {
        if (reason == null) return "Operation failed";
        String r = reason.trim();
        if (r.length() <= 255) return r;
        return r.substring(0, 255);
    }

    private Transaction getTx(Long txId) {
        return transactionRepository.findById(txId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }
}
