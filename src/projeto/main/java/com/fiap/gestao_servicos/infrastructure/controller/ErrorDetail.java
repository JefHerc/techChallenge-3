package com.fiap.gestao_servicos.infrastructure.controller;

import java.time.LocalDateTime;

public record ErrorDetail(
        String codigo,
        String mensagem,
        LocalDateTime timestamp
) {
}


