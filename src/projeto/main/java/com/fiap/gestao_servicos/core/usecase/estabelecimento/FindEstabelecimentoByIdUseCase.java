package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class FindEstabelecimentoByIdUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindEstabelecimentoByIdUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento findById(Long id) {
        return estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Estabelecimento",
                        id)));
    }
}

