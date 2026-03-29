package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;

public class DeleteAvaliacaoUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    public DeleteAvaliacaoUseCase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public void deleteById(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação não encontrada para o id: " + id);
        }
        avaliacaoRepository.deleteById(id);
    }
}


