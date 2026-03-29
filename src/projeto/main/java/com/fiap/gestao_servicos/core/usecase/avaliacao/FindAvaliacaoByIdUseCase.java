package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;

public class FindAvaliacaoByIdUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    public FindAvaliacaoByIdUseCase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao findById(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Avaliação",
                        id)));
    }
}


