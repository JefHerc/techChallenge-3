package com.fiap.gestao_servicos.core.domain;

import java.math.BigDecimal;

public class ServicoOfertadoSearch {

    private final Long servicoId;
    private final String servicoNome;
    private final BigDecimal preco;
    private final ProfissionalServicoInfo profissional;

    public ServicoOfertadoSearch(Long servicoId,
                                 String servicoNome,
                                 BigDecimal preco,
                                 ProfissionalServicoInfo profissional) {
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.preco = preco;
        this.profissional = profissional;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public ProfissionalServicoInfo getProfissional() {
        return profissional;
    }
}