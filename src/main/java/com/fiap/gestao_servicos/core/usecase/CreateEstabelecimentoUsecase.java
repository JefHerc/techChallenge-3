package com.fiap.gestao_servicos.core.usecase;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoRepository;

public class CreateEstabelecimentoUsecase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public CreateEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository){
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento create(Estabelecimento estabelecimento){
        System.out.println(estabelecimento);
        return null;
    }
}
