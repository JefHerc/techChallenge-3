package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class DeleteEstabelecimentoUsecase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public DeleteEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public void deleteById(Long id) {
        if (!estabelecimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado para o id: " + id);
        }
        estabelecimentoRepository.deleteById(id);
    }
}