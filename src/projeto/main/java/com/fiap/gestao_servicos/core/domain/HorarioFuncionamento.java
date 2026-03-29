package com.fiap.gestao_servicos.core.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class HorarioFuncionamento {

    private Long id;
    private DayOfWeek diaSemana;
    private LocalTime abertura;
    private LocalTime fechamento;
    private boolean fechado;
    private String descricao;

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
        this.descricao = null;
    }

    /**
     * Create a HorarioFuncionamento from a raw textual description (keeps raw text).
     * Useful when the persisted format is a free-text schedule.
     */
    public HorarioFuncionamento(String descricao) {
        this.id = null;
        this.diaSemana = null;
        this.abertura = null;
        this.fechamento = null;
        this.fechado = false;
        this.descricao = descricao == null ? null : descricao.trim();
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

    public String getDescricao() {
        return descricao;
    }

    public static HorarioFuncionamento fromString(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return new HorarioFuncionamento(s.trim());
    }

    @Override
    public String toString() {
        if (descricao != null) {
            return descricao;
        }
        if (diaSemana == null) {
            return "";
        }
        String dia = diaSemana.getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        if (fechado) {
            return String.format("%s: fechado", dia);
        }
        String aberturaStr = abertura != null ? abertura.toString() : "";
        String fechamentoStr = fechamento != null ? fechamento.toString() : "";
        return String.format("%s %s-%s", dia, aberturaStr, fechamentoStr).trim();
    }

    public String getDiaSemanaPt() {
        return diaSemana == null ? null : diaSemana.getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
    }

}


