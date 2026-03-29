package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.NameAlreadyExistsException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

public class UpdateServicoUseCase {

    private final ServicoRepository servicoRepository;

    public UpdateServicoUseCase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico update(Long id, Servico servico) {
        Servico atual = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Serviço",
                        id)));

        if (!atual.getNomeAsString().equalsIgnoreCase(servico.getNomeAsString())
            && servicoRepository.existsByNomeIgnoreCase(servico.getNomeAsString())) {
            throw new NameAlreadyExistsException("Serviço ");
        }

        Servico atualizado = servicoRepository.update(id, servico);
        if (atualizado == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Serviço", id));
        }

        return atualizado;
    }
}

