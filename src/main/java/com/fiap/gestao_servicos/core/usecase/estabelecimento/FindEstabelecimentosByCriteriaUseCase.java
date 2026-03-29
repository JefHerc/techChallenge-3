package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoSearchResult;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class FindEstabelecimentosByCriteriaUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindEstabelecimentosByCriteriaUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public PageResult<Estabelecimento> findByCriteria(EstabelecimentoFilter filter, PageQuery pageQuery) {
        return estabelecimentoRepository.findByCriteria(filter, pageQuery);
    }

    public PageResult<EstabelecimentoSearchResult> findByCriteriaWithServices(EstabelecimentoFilter filter, PageQuery pageQuery) {
        return estabelecimentoRepository.findByCriteriaWithServices(filter, pageQuery);
    }
}


