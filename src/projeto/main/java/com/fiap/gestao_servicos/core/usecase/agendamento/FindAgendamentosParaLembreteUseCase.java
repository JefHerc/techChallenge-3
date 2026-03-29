package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class FindAgendamentosParaLembreteUseCase {

    private final AgendamentoRepository agendamentoRepository;

    public FindAgendamentosParaLembreteUseCase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public List<Agendamento> findByJanela(AgendamentoStatus status, LocalDateTime inicio, LocalDateTime fim) {
        if (status == null) {
            throw new IllegalArgumentException("status não pode ser nulo");
        }
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("janela de busca inválida");
        }
        if (!inicio.isBefore(fim)) {
            throw new IllegalArgumentException("início deve ser anterior ao fim");
        }

        return agendamentoRepository.findByStatusAndDataHoraInicioBetween(status, inicio, fim);
    }
}

