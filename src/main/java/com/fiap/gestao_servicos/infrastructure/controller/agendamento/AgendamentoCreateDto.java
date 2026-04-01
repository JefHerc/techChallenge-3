package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = false)
@Schema(description = "Payload para criacao de agendamento")
public class AgendamentoCreateDto {

    @Schema(description = "ID do profissional", example = "1")
    @NotNull(message = "Profissional é obrigatório")
    private Long profissionalId;

    @Schema(description = "ID do servico", example = "1")
    @NotNull(message = "Serviço é obrigatório")
    private Long servicoId;

    @Schema(description = "ID do estabelecimento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long estabelecimentoId;

    @Schema(description = "ID do cliente", example = "1")
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

    @Schema(
            description = "Data e hora de inicio no formato ISO-8601 date-time",
            example = "2026-04-20T10:00:00",
            type = "string",
            format = "date-time"
    )
    @NotNull(message = "Data/hora de início é obrigatória")
    private LocalDateTime dataHoraInicio;

    @Schema(hidden = true)
    @Null(message = "Status não deve ser informado na criação do agendamento")
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
