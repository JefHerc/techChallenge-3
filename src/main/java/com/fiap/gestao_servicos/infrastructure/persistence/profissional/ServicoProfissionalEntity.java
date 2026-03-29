package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "servico_profissional")
public class ServicoProfissionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoEntity servico;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalEntity profissional;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    public ServicoProfissionalEntity() {}

    public ServicoProfissionalEntity(ServicoEntity servico, ProfissionalEntity profissional, BigDecimal valor) {
        this.servico = servico;
        this.profissional = profissional;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicoEntity getServico() {
        return servico;
    }

    public void setServico(ServicoEntity servico) {
        this.servico = servico;
    }

    public ProfissionalEntity getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalEntity profissional) {
        this.profissional = profissional;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}


