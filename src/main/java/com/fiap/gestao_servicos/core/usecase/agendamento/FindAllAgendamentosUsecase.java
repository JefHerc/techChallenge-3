package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

import java.util.List;

public class FindAllAgendamentosUsecase {

    private final AgendamentoRepository agendamentoRepository;

    public FindAllAgendamentosUsecase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public List<Agendamento> findAll() {
        return agendamentoRepository.findAll();
    }
}