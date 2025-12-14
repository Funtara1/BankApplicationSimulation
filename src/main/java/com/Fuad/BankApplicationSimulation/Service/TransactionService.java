package com.Fuad.BankApplicationSimulation.Service;

import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.ResponseDTO.TransactionResponse;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction deposit(Long toAccountId, BigDecimal amount);
    Transaction withdraw(Long fromAccountId, BigDecimal amount);
    Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
    List<TransactionResponse> getTransactionsByAccountId(Long accountId);
    Transaction getTransactionById(Long id);
}
