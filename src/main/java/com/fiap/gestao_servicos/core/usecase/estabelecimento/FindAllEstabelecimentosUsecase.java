package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

import java.util.List;

public class FindAllEstabelecimentosUsecase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindAllEstabelecimentosUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public List<Estabelecimento> findAll() {
        return estabelecimentoRepository.findAll();
    }
}