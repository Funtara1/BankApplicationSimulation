package com.fuad.bank.application.service.impl;

import com.fuad.bank.domain.entity.Account;
import com.fuad.bank.domain.entity.Transaction;
import com.fuad.bank.domain.enums.TransactionStatus;
import com.fuad.bank.domain.enums.TransactionType;
import com.fuad.bank.domain.exception.NotFoundException;
import com.fuad.bank.infrastructure.persistence.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionAuditService {

    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction createPending(TransactionType type, BigDecimal amount) {
        Transaction tx = new Transaction();
        tx.setTransactionType(type);
        tx.setAmount(amount);
        tx.setTransactionStatus(TransactionStatus.PENDING);
        return transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void attachAccounts(Long txId, Long fromAccountId, Long toAccountId) {
        Transaction tx = getTx(txId);

        if (fromAccountId != null) {
            tx.setFromAccount(entityManager.getReference(Account.class, fromAccountId));
        }
        if (toAccountId != null) {
            tx.setToAccount(entityManager.getReference(Account.class, toAccountId));
        }

        transactionRepository.save(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompletedSingle(Long txId, BigDecimal oldBalance, BigDecimal newBalance) {
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
    public void markCompletedTransfer(
            Long txId,
            BigDecimal oldFrom, BigDecimal newFrom,
            BigDecimal oldTo, BigDecimal newTo
    ) {
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
    public void markFailed(Long txId, Throwable error) {
        Transaction tx = getTx(txId);
        tx.setTransactionStatus(TransactionStatus.FAILED);
        tx.setErrorMessage(shortReason(error));
        transactionRepository.save(tx);
    }

    private Transaction getTx(Long txId) {
        return transactionRepository.findById(txId)
                .orElseThrow(() -> new NotFoundException(
                        "Transaction not found",
                        Map.of("transactionId", txId)
                ));
    }

    private String shortReason(Throwable error) {
        if (error == null) return "Operation failed";

        String msg = error.getMessage();
        String base = (msg == null || msg.trim().isEmpty())
                ? error.getClass().getSimpleName()
                : error.getClass().getSimpleName() + ": " + msg.trim();

        return base.length() <= 255 ? base : base.substring(0, 255);
    }
}
