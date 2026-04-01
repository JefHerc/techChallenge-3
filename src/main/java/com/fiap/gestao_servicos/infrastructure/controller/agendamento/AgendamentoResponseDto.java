package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados de agendamento retornados pela API")
public class AgendamentoResponseDto {

    @Schema(description = "Identificador do agendamento", example = "1")
    private Long id;
    @Schema(description = "ID do profissional", example = "1")
    private Long profissionalId;
    @Schema(description = "Nome do profissional", example = "Carla Souza", minLength = 2, maxLength = 120)
    private String profissionalNome;
    @Schema(description = "ID do servico", example = "1")
    private Long servicoId;
    @Schema(description = "Nome do servico", example = "CORTE", minLength = 2, maxLength = 120)
    private String servicoNome;
    @Schema(description = "ID do estabelecimento", example = "1")
    private Long estabelecimentoId;
    @Schema(description = "Nome do estabelecimento", example = "Studio Bela Vida", minLength = 2, maxLength = 120)
    private String estabelecimentoNome;
    @Schema(description = "ID do cliente", example = "1")
    private Long clienteId;
    @Schema(description = "Nome do cliente", example = "João Silva", minLength = 2, maxLength = 120)
    private String clienteNome;
        @Schema(
            description = "Data e hora de inicio no formato ISO-8601 date-time",
            example = "2026-04-20T10:00:00",
            type = "string",
            format = "date-time"
        )
    private LocalDateTime dataHoraInicio;
        @Schema(
            description = "Data e hora de fim no formato ISO-8601 date-time",
            example = "2026-04-20T11:00:00",
            type = "string",
            format = "date-time"
        )
    private LocalDateTime dataHoraFim;
        @Schema(
            description = "Status do agendamento",
            example = "AGENDADO",
            allowableValues = {"AGENDADO", "CANCELADO", "CONCLUIDO"}
        )
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public String getProfissionalNome() {
        return profissionalNome;
    }

    public void setProfissionalNome(String profissionalNome) {
        this.profissionalNome = profissionalNome;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public Long getEstabelecimentoId() {
        return estabelecimentoId;
    }

    public void setEstabelecimentoId(Long estabelecimentoId) {
        this.estabelecimentoId = estabelecimentoId;
    }

    public String getEstabelecimentoNome() {
        return estabelecimentoNome;
    }

    public void setEstabelecimentoNome(String estabelecimentoNome) {
        this.estabelecimentoNome = estabelecimentoNome;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

