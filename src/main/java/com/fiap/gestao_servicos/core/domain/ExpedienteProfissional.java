package com.fiap.gestao_servicos.core.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ExpedienteProfissional {

    private Long id;
    private DayOfWeek diaSemana;
    private LocalTime inicioTurno;
    private LocalTime fimTurno;
    private LocalTime inicioIntervalo;
    private LocalTime fimIntervalo;

    public ExpedienteProfissional(Long id, DayOfWeek diaSemana, LocalTime inicioTurno, LocalTime fimTurno, LocalTime inicioIntervalo, LocalTime fimIntervalo) {
        this.id = id;
        if (diaSemana == null) {
            throw new IllegalArgumentException("Dia da semana não pode ser nulo");
        }
        this.diaSemana = diaSemana;

        if (inicioTurno == null) {
            throw new IllegalArgumentException("Hora de início do turno não pode ser nula");
        }
        this.inicioTurno = inicioTurno;

        if (fimTurno == null) {
            throw new IllegalArgumentException("Hora de fim do turno não pode ser nula");
        }
        if (!fimTurno.isAfter(inicioTurno)) {
            throw new IllegalArgumentException("Hora de fim do turno deve ser depois da hora de início do turno");
        }
        this.fimTurno = fimTurno;

        if (inicioIntervalo != null && (inicioIntervalo.isBefore(inicioTurno) || inicioIntervalo.isAfter(fimTurno))) {
            throw new IllegalArgumentException("Hora de início do intervalo deve estar dentro do turno");
        }
        this.inicioIntervalo = inicioIntervalo;

        if (fimIntervalo != null && (fimIntervalo.isBefore(inicioTurno) || fimIntervalo.isAfter(fimTurno))) {
            throw new IllegalArgumentException("Hora de fim do intervalo deve estar dentro do turno");
        }
        if (inicioIntervalo != null && fimIntervalo != null && !fimIntervalo.isAfter(inicioIntervalo)) {
            throw new IllegalArgumentException("Hora de fim do intervalo deve ser depois da hora de início do intervalo");
        }
        this.fimIntervalo = fimIntervalo;
    }

    public Long getId() {
        return id;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getInicioTurno() {
        return inicioTurno;
    }

    public LocalTime getFimTurno() {
        return fimTurno;
    }

    public LocalTime getInicioIntervalo() {
        return inicioIntervalo;
    }

    public LocalTime getFimIntervalo() {
        return fimIntervalo;
    }
}
