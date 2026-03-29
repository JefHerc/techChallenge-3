package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindEstabelecimentosByCriteriaUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindEstabelecimentosByCriteriaUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Page<Estabelecimento> findByCriteria(EstabelecimentoFilter filter, Pageable pageable) {
        return estabelecimentoRepository.findByCriteria(filter, pageable);
    }
}


