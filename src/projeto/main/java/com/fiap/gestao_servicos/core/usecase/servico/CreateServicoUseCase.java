package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.NameAlreadyExistsException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

public class CreateServicoUseCase {

    private final ServicoRepository servicoRepository;

    public CreateServicoUseCase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico create(Servico servico) {
        if (servicoRepository.existsByNomeIgnoreCase(servico.getNomeAsString())) {
            throw new NameAlreadyExistsException("Serviço ");
        }
        return servicoRepository.create(servico);
    }
}

