package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import jakarta.validation.constraints.NotBlank;

public class ExpedienteProfissionalDto {

    @NotBlank(message = "Dia da semana é obrigatório")
    private String diaSemana; 
    @NotBlank(message = "Início do turno é obrigatório")
    private String inicioTurno; 
    @NotBlank(message = "Fim do turno é obrigatório")
    private String fimTurno; 
    private String inicioIntervalo;
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


