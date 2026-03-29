package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import java.math.BigDecimal;

public class ServicoOfertadoDto {

    private Long servicoId;
    private String servicoNome;
    private BigDecimal preco;
    private ProfissionalServicoDto profissional;

    public ServicoOfertadoDto() {}

    public ServicoOfertadoDto(Long servicoId, String servicoNome, BigDecimal preco, ProfissionalServicoDto profissional) {
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.preco = preco;
        this.profissional = profissional;
    }

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public ProfissionalServicoDto getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalServicoDto profissional) {
        this.profissional = profissional;
    }
}
