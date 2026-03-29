package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class DeleteAgendamentoUseCase {

    private final AgendamentoRepository agendamentoRepository;

    public DeleteAgendamentoUseCase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public void deleteById(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Agendamento",
                        id)));

        if (AgendamentoStatus.CONCLUIDO.equals(agendamento.getStatus())) {
            throw new IllegalArgumentException("Nao e permitido excluir agendamento com status CONCLUIDO.");
        }

        agendamentoRepository.deleteById(id);
    }
}


