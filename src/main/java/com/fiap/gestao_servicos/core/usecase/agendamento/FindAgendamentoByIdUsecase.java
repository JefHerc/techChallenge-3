package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class FindAgendamentoByIdUsecase {

    private final AgendamentoRepository agendamentoRepository;

    public FindAgendamentoByIdUsecase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Agendamento findById(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id);
        if (agendamento == null) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o id: " + id);
        }
        return agendamento;
    }
}