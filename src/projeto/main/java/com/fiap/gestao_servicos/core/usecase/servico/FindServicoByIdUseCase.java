package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

public class FindServicoByIdUseCase {

    private final ServicoRepository servicoRepository;

    public FindServicoByIdUseCase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico findById(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Serviço",
                        id)));
    }
}

