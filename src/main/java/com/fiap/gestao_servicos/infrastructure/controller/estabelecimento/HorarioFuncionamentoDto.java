package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class HorarioFuncionamentoDto {

    @Schema(
        description = "Dia da semana em portugues (pt-BR)",
        example = "segunda-feira",
        allowableValues = {
            "segunda-feira",
            "terca-feira",
            "quarta-feira",
            "quinta-feira",
            "sexta-feira",
            "sabado",
            "domingo"
        }
    )
    @NotBlank(message = "Dia da semana é obrigatório")
    private String diaSemana;
    @Schema(
        description = "Horario de abertura no formato HH:mm ou HH:mm:ss (ISO-8601 parcial)",
        example = "09:00",
        type = "string",
        format = "time",
        pattern = "^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"
    )
    private String abertura;
    @Schema(
        description = "Horario de fechamento no formato HH:mm ou HH:mm:ss (ISO-8601 parcial)",
        example = "18:00",
        type = "string",
        format = "time",
        pattern = "^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"
    )
    private String fechamento;
    private Boolean fechado;

    public HorarioFuncionamentoDto() {}

    public HorarioFuncionamentoDto(String diaSemana, String abertura, String fechamento, Boolean fechado) {
        this.diaSemana = diaSemana;
        this.abertura = abertura;
        this.fechamento = fechamento;
        this.fechado = fechado;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getAbertura() {
        return abertura;
    }

    public void setAbertura(String abertura) {
        this.abertura = abertura;
    }

    public String getFechamento() {
        return fechamento;
    }

    public void setFechamento(String fechamento) {
        this.fechamento = fechamento;
    }

    public Boolean isFechado() {
        return fechado;
    }

    public void setFechado(Boolean fechado) {
        this.fechado = fechado;
    }
}

