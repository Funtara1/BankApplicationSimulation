package com.fuad.bank.domain.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class DuplicateException extends AppException {
    public DuplicateException(String message, Map<String, Object> details) {
        super("DUPLICATE_ACCOUNT", HttpStatus.CONFLICT, message, details);
    }
}
