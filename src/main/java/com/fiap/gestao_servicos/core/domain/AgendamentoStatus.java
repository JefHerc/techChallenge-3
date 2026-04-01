package com.fiap.gestao_servicos.core.domain;

public enum AgendamentoStatus {
    AGENDADO,
    CANCELADO,
    CONCLUIDO;

    public static AgendamentoStatus parse(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio");
        }
        try {
            return AgendamentoStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status inválido. Valores permitidos: AGENDADO, CANCELADO, CONCLUIDO");
        }
    }
}


