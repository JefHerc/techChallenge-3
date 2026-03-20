package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;

public class FindAvaliacaoByIdUsecase {

    private final AvaliacaoRepository avaliacaoRepository;

    public FindAvaliacaoByIdUsecase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao findById(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id);
        if (avaliacao == null) {
            throw new ResourceNotFoundException("Avaliação não encontrada para o id: " + id);
        }
        return avaliacao;
    }
}
