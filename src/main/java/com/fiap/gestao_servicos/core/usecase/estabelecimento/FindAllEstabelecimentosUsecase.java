package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class FindAllEstabelecimentosUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindAllEstabelecimentosUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public PageResult<Estabelecimento> findAll(PageQuery pageQuery) {
        return estabelecimentoRepository.findAll(pageQuery);
    }
}

