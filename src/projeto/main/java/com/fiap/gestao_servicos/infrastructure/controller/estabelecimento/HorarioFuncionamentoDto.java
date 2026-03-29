package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import jakarta.validation.constraints.NotBlank;

public class HorarioFuncionamentoDto {

    @NotBlank(message = "Dia da semana é obrigatório")
    private String diaSemana;
    private String abertura;
    private String fechamento;
    private boolean fechado;

    public HorarioFuncionamentoDto() {}

    public HorarioFuncionamentoDto(String diaSemana, String abertura, String fechamento, boolean fechado) {
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

    public boolean isFechado() {
        return fechado;
    }

    public void setFechado(boolean fechado) {
        this.fechado = fechado;
    }
}

