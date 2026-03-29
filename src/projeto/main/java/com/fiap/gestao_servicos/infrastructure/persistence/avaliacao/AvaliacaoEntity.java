package com.fiap.gestao_servicos.infrastructure.persistence.avaliacao;

import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "avaliacao")
public class AvaliacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private AgendamentoEntity agendamento;

    @Column(nullable = false)
    private double notaEstabelecimento;

    @Column(nullable = false)
    private double notaProfissional;

    @Column(length = 500)
    private String comentarioEstabelecimento;

    @Column(length = 500)
    private String comentarioProfissional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgendamentoEntity getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoEntity agendamento) {
        this.agendamento = agendamento;
    }

    public double getNotaEstabelecimento() {
        return notaEstabelecimento;
    }

    public void setNotaEstabelecimento(double notaEstabelecimento) {
        this.notaEstabelecimento = notaEstabelecimento;
    }

    public double getNotaProfissional() {
        return notaProfissional;
    }

    public void setNotaProfissional(double notaProfissional) {
        this.notaProfissional = notaProfissional;
    }

    public String getComentarioEstabelecimento() {
        return comentarioEstabelecimento;
    }

    public void setComentarioEstabelecimento(String comentarioEstabelecimento) {
        this.comentarioEstabelecimento = comentarioEstabelecimento;
    }

    public String getComentarioProfissional() {
        return comentarioProfissional;
    }

    public void setComentarioProfissional(String comentarioProfissional) {
        this.comentarioProfissional = comentarioProfissional;
    }
}

