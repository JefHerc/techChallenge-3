package com.fiap.gestao_servicos.core.domain;

import java.util.List;

public class EstabelecimentoSearchResult {

    private final Estabelecimento estabelecimento;
    private final List<ServicoOfertadoSearch> servicosOfertados;

    public EstabelecimentoSearchResult(Estabelecimento estabelecimento,
                                       List<ServicoOfertadoSearch> servicosOfertados) {
        this.estabelecimento = estabelecimento;
        this.servicosOfertados = servicosOfertados;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public List<ServicoOfertadoSearch> getServicosOfertados() {
        return servicosOfertados;
    }
}