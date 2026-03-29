package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;

@Embeddable
public class HorarioFuncionamentoEmbeddable {

    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "abertura")
    private LocalTime abertura;

    @Column(name = "fechamento")
    private LocalTime fechamento;

    @Column(name = "fechado")
    private Boolean fechado;

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalTime abertura) {
        this.abertura = abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }

    public void setFechamento(LocalTime fechamento) {
        this.fechamento = fechamento;
    }

    public Boolean getFechado() {
        return fechado;
    }

    public void setFechado(Boolean fechado) {
        this.fechado = fechado;
    }
}

