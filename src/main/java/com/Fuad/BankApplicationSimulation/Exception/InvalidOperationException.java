package com.Fuad.BankApplicationSimulation.Exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class InvalidOperationException extends AppException {
    public InvalidOperationException(String message, Map<String, Object> details) {
        super("INVALID_OPERATION", HttpStatus.BAD_REQUEST, message, details);
    }
}
