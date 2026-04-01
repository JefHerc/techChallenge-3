package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class ExpedienteProfissionalDto {

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
        description = "Horario de inicio do turno no formato HH:mm ou HH:mm:ss (ISO-8601 parcial)",
        example = "08:00",
        type = "string",
        format = "time",
        pattern = "^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"
    )
    @NotBlank(message = "Início do turno é obrigatório")
    private String inicioTurno;
    @Schema(
        description = "Horario de fim do turno no formato HH:mm ou HH:mm:ss (ISO-8601 parcial)",
        example = "17:00",
        type = "string",
        format = "time",
        pattern = "^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"
    )
    @NotBlank(message = "Fim do turno é obrigatório")
    private String fimTurno;
    @Schema(
        description = "Horario de inicio do intervalo no formato HH:mm ou HH:mm:ss (ISO-8601 parcial)",
        example = "12:00",
        type = "string",
        format = "time",
        pattern = "^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"
    )
    private String inicioIntervalo;
    @Schema(
        description = "Horario de fim do intervalo no formato HH:mm ou HH:mm:ss (ISO-8601 parcial)",
        example = "13:00",
        type = "string",
        format = "time",
        pattern = "^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"
    )
    private String fimIntervalo;

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getInicioTurno() {
        return inicioTurno;
    }

    public void setInicioTurno(String inicioTurno) {
        this.inicioTurno = inicioTurno;
    }

    public String getFimTurno() {
        return fimTurno;
    }

    public void setFimTurno(String fimTurno) {
        this.fimTurno = fimTurno;
    }

    public String getInicioIntervalo() {
        return inicioIntervalo;
    }

    public void setInicioIntervalo(String inicioIntervalo) {
        this.inicioIntervalo = inicioIntervalo;
    }

    public String getFimIntervalo() {
        return fimIntervalo;
    }

    public void setFimIntervalo(String fimIntervalo) {
        this.fimIntervalo = fimIntervalo;
    }
}


