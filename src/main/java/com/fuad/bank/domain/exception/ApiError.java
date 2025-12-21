package com.fuad.bank.domain.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        String path,
        String code,
        String message,
        Map<String, Object> details
) {}