package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

public class DeleteServicoUsecase {

    private final ServicoRepository servicoRepository;

    public DeleteServicoUsecase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public void deleteById(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado para o id: " + id);
        }
        servicoRepository.deleteById(id);
    }
}