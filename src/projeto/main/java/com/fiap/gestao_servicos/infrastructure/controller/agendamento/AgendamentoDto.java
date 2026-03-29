package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AgendamentoDto {

    @NotNull(message = "Profissional é obrigatório")
    private Long profissionalId;
    @NotNull(message = "Serviço é obrigatório")
    private Long servicoId;
    private Long estabelecimentoId;
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    @NotNull(message = "Data/hora de início é obrigatória")
    private LocalDateTime dataHoraInicio;
    @NotBlank(message = "Status é obrigatório")
    private String status;

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public Long getEstabelecimentoId() {
        return estabelecimentoId;
    }

    public void setEstabelecimentoId(Long estabelecimentoId) {
        this.estabelecimentoId = estabelecimentoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

