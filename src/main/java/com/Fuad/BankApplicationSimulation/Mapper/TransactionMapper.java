package com.Fuad.BankApplicationSimulation.Mapper;

import com.Fuad.BankApplicationSimulation.DTO.TransactionDTO.ResponseDTO.TransactionResponse;
import com.Fuad.BankApplicationSimulation.Entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction t) {
        if (t == null) {
            return null;
        }

        return TransactionResponse.builder()
                .date(t.getTimestamp())
                .type(t.getTransactionType().getDescription())
                .amount(t.getAmount())
                .oldBalance(t.getOldBalance())
                .newBalance(t.getNewBalance())
                .oldToBalance(t.getOldToBalance())
                .newToBalance(t.getNewToBalance())
                .fromAccount(
                        t.getFromAccount() != null
                                ? t.getFromAccount().getAccountNumber()
                                : null
                )
                .toAccount(
                        t.getToAccount() != null
                                ? t.getToAccount().getAccountNumber()
                                : null
                )
                .status(t.getTransactionStatus())
                .reason(t.getErrorMessage())
                .build();
    }
}

