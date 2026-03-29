package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllEstabelecimentosUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindAllEstabelecimentosUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Page<Estabelecimento> findAll(Pageable pageable) {
        return estabelecimentoRepository.findAll(pageable);
    }
}

