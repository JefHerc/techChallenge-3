package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class DeleteAgendamentoUsecase {

    private final AgendamentoRepository agendamentoRepository;

    public DeleteAgendamentoUsecase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public void deleteById(Long id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o id: " + id);
        }
        agendamentoRepository.deleteById(id);
    }
}