package com.fuad.bank.application.service;

import com.fuad.bank.api.dto.TransactionDTO.RequestDTO.TransactionFilterRequest;
import com.fuad.bank.api.dto.TransactionDTO.ResponseDTO.TransactionResponse;
import com.fuad.bank.domain.entity.Transaction;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction deposit(Long toAccountId, BigDecimal amount);
    Transaction withdraw(Long fromAccountId, BigDecimal amount);
    Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
    List<TransactionResponse> getTransactionsByAccountId(Long accountId);
    Transaction getTransactionById(Long id);

    Page<TransactionResponse> filter(
            TransactionFilterRequest filter,
            int page,
            int size
    );
}
