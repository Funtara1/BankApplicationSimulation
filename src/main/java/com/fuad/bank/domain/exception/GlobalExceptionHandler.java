package com.fuad.bank.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAppException(AppException ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                req.getRequestURI(),
                ex.getCode(),
                ex.getMessage(),
                ex.getDetails() == null ? Map.of() : ex.getDetails()
        );

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        Map<String, Object> fieldErrors = new LinkedHashMap<>();

        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            // если по одному полю несколько ошибок, можно перезаписать, для junior/middle это ок
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        ApiError body = new ApiError(
                Instant.now(),
                req.getRequestURI(),
                "VALIDATION_ERROR",
                "Validation failed",
                Map.of("fields", fieldErrors)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest req
    ) {
        Map<String, Object> violations = new LinkedHashMap<>();

        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String path = v.getPropertyPath() == null ? "unknown" : v.getPropertyPath().toString();
            violations.put(path, v.getMessage());
        }

        ApiError body = new ApiError(
                Instant.now(),
                req.getRequestURI(),
                "VALIDATION_ERROR",
                "Validation failed",
                Map.of("violations", violations)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest req
    ) {
        ApiError body = new ApiError(
                Instant.now(),
                req.getRequestURI(),
                "VALIDATION_ERROR",
                "Malformed JSON or invalid request body",
                Map.of("reason", shortReason(ex))
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest req) {
        // Минимально логируем. Если есть логгер, лучше использовать его.
        ex.printStackTrace();

        ApiError body = new ApiError(
                Instant.now(),
                req.getRequestURI(),
                "INTERNAL_ERROR",
                "Unexpected server error",
                Map.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private String shortReason(Throwable error) {
        if (error == null) return "Unknown error";
        String msg = error.getMessage();
        String base = (msg == null || msg.trim().isEmpty())
                ? error.getClass().getSimpleName()
                : error.getClass().getSimpleName() + ": " + msg.trim();

        return base.length() <= 255 ? base : base.substring(0, 255);
    }
}
