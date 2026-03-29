package com.fiap.gestao_servicos.infrastructure.controller;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String code,
        String message,
        Instant timestamp,
        String path,
        List<ApiFieldError> errors
) {
}

