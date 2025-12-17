package com.Fuad.BankApplicationSimulation.Exception;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        String path,
        String code,
        String message,
        Map<String, Object> details
) {}