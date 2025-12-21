package com.fuad.bank.domain.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class InsufficientFundsException extends AppException {
    public InsufficientFundsException(String message, Map<String, Object> details) {
        super("INSUFFICIENT_FUNDS", HttpStatus.BAD_REQUEST, message, details);
    }
}
