package com.Fuad.BankApplicationSimulation.Exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class NotFoundException extends AppException {
    public NotFoundException(String message, Map<String, Object> details) {
        super("NOT_FOUND", HttpStatus.NOT_FOUND, message, details);
    }
}
