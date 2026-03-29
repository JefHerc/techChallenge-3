package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import java.math.BigDecimal;

public class ServicoProfissionalResponseDto {

    private Long servicoId;
    private String servicoNome;
    private BigDecimal valor;

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}


