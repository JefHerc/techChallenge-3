package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.NameAlreadyExistsException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

public class UpdateServicoUsecase {

    private final ServicoRepository servicoRepository;

    public UpdateServicoUsecase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico update(Long id, Servico servico) {
        Servico atual = servicoRepository.findById(id);
        if (atual == null) {
            throw new ResourceNotFoundException("Serviço não encontrado para o id: " + id);
        }

        if (!atual.getNome().equalsIgnoreCase(servico.getNome())
                && servicoRepository.existsByNomeIgnoreCase(servico.getNome())) {
            throw new NameAlreadyExistsException("Serviço ");
        }

        Servico atualizado = servicoRepository.update(id, servico);
        if (atualizado == null) {
            throw new ResourceNotFoundException("Serviço não encontrado para o id: " + id);
        }

        return atualizado;
    }
}