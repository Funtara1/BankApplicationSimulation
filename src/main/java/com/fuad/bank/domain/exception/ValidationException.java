package com.fuad.bank.domain.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationException extends AppException {
    public ValidationException(String message, Map<String, Object> details) {
        super("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, message, details);
    }
}

