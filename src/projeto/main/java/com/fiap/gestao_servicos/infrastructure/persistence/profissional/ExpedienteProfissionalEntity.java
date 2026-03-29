package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "expediente_profissional")
public class ExpedienteProfissionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalEntity profissional;

    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

    @Column(name = "inicio_turno", nullable = false)
    private LocalTime inicioTurno;

    @Column(name = "fim_turno", nullable = false)
    private LocalTime fimTurno;

    @Column(name = "inicio_intervalo")
    private LocalTime inicioIntervalo;

    @Column(name = "fim_intervalo")
    private LocalTime fimIntervalo;

    public ExpedienteProfissionalEntity() {}

    public ExpedienteProfissionalEntity(ProfissionalEntity profissional, String diaSemana, LocalTime inicioTurno, LocalTime fimTurno, LocalTime inicioIntervalo, LocalTime fimIntervalo) {
        this.profissional = profissional;
        this.diaSemana = diaSemana;
        this.inicioTurno = inicioTurno;
        this.fimTurno = fimTurno;
        this.inicioIntervalo = inicioIntervalo;
        this.fimIntervalo = fimIntervalo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfissionalEntity getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalEntity profissional) {
        this.profissional = profissional;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getInicioTurno() {
        return inicioTurno;
    }

    public void setInicioTurno(LocalTime inicioTurno) {
        this.inicioTurno = inicioTurno;
    }

    public LocalTime getFimTurno() {
        return fimTurno;
    }

    public void setFimTurno(LocalTime fimTurno) {
        this.fimTurno = fimTurno;
    }

    public LocalTime getInicioIntervalo() {
        return inicioIntervalo;
    }

    public void setInicioIntervalo(LocalTime inicioIntervalo) {
        this.inicioIntervalo = inicioIntervalo;
    }

    public LocalTime getFimIntervalo() {
        return fimIntervalo;
    }

    public void setFimIntervalo(LocalTime fimIntervalo) {
        this.fimIntervalo = fimIntervalo;
    }
}


