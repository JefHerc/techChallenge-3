package com.fiap.gestao_servicos.core.domain;

import java.math.BigDecimal;

public class ServicoProfissional {

    private Long id;
    private Servico servico;
    private Profissional profissional;
    private BigDecimal valor;

    public ServicoProfissional(Long id, Servico servico, Profissional profissional, BigDecimal valor) {
        this.id = id;
        
        validarValor(valor);

        this.servico = servico;
        this.profissional = profissional;
        this.valor = valor;
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do serviço não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do serviço não pode ser negativo");
        }
    }

    public Long getId() {
        return id;
    }

    public Servico getServico() {
        return servico;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
