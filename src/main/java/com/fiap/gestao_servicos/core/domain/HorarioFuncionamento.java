package com.fiap.gestao_servicos.core.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioFuncionamento {

    private Long id;
    private DayOfWeek diaSemana;
    private LocalTime abertura;
    private LocalTime fechamento;
    private boolean fechado;

    public HorarioFuncionamento(Long id, DayOfWeek diaSemana, LocalTime abertura, LocalTime fechamento, boolean fechado) {
        this.id = id;
        if (diaSemana == null) {
            throw new IllegalArgumentException("Dia da semana não pode ser nulo");
        }
        this.diaSemana = diaSemana;

        if (abertura == null && !fechado) {
            throw new IllegalArgumentException("Hora de abertura não pode ser nula");
        }
        this.abertura = abertura;

        if (fechamento == null && !fechado) {
            throw new IllegalArgumentException("Hora de fechamento não pode ser nula");
        }
        this.fechamento = fechamento;

        if (!fechado && !fechamento.isAfter(abertura)) {
            throw new IllegalArgumentException("Hora de fechamento deve ser depois da hora de abertura");
        }
        this.fechado = fechado;
    }

    public Long getId() {
        return id;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }

    public boolean isFechado() {
        return fechado;
    }

}
