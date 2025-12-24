package com.fuad.bank.infrastructure.messaging.dto;

import com.fuad.bank.domain.enums.TransactionStatus;
import com.fuad.bank.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionEvent(
        Long transactionId,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        Long fromAccountId,
        Long toAccountId,
        LocalDateTime timestamp,
        String errorMessage
) {}
