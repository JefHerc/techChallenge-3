package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class FindAgendamentoByIdUseCase {

    private final AgendamentoRepository agendamentoRepository;

    public FindAgendamentoByIdUseCase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Agendamento findById(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Agendamento",
                        id)));
    }
}

