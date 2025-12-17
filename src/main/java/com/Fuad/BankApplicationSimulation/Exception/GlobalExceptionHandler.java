package com.Fuad.BankApplicationSimulation.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAppException(AppException ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                req.getRequestURI(),
                ex.getCode(),
                ex.getMessage(),
                ex.getDetails()
        );

        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
