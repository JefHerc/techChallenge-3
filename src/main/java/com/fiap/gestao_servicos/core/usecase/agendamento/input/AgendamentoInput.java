package com.fiap.gestao_servicos.core.usecase.agendamento.input;

import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;

import java.time.LocalDateTime;

/**
 * Classe que representa um agendamento com apenas as referencias de IDs.
 * Esta e uma abstracao para o fluxo de entrada, evitando a criacao de objetos
 * ficticios no mapper. Os repositorios carregarao as entidades reais do banco.
 */
public class AgendamentoInput {

    private Long profissionalId;
    private Long servicoId;
    private Long estabelecimentoId;
    private Long clienteId;
    private LocalDateTime dataHoraInicio;
    private AgendamentoStatus status;

    public AgendamentoInput(Long profissionalId, Long servicoId, Long estabelecimentoId,
                            Long clienteId, LocalDateTime dataHoraInicio, AgendamentoStatus status) {
        this.profissionalId = validarId(profissionalId, "profissionalId");
        this.servicoId = validarId(servicoId, "servicoId");
        this.estabelecimentoId = validarId(estabelecimentoId, "estabelecimentoId");
        this.clienteId = validarId(clienteId, "clienteId");
        this.dataHoraInicio = dataHoraInicio;
        this.status = status;
    }

    private static Long validarId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " deve ser informado com um valor positivo");
        }
        return id;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public Long getEstabelecimentoId() {
        return estabelecimentoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public AgendamentoStatus getStatus() {
        return status;
    }
}