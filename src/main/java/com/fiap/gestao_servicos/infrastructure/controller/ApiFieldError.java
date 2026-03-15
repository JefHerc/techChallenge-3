package com.fiap.gestao_servicos.infrastructure.controller;

public record ApiFieldError(
        String field,
        String detail
) {
}