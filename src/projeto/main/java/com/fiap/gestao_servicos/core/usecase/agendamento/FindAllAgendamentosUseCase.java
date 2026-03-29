package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class FindAllAgendamentosUseCase {

    private final AgendamentoRepository agendamentoRepository;

    public FindAllAgendamentosUseCase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Page<Agendamento> findAll(Pageable pageable) {
        return agendamentoRepository.findAll(pageable);
    }

    public Page<Agendamento> findByEstabelecimentoId(Long estabelecimentoId, Pageable pageable) {
        if (estabelecimentoId == null || estabelecimentoId <= 0) {
            throw new IllegalArgumentException("estabelecimentoId deve ser informado com um valor positivo");
        }
        return agendamentoRepository.findByEstabelecimentoId(estabelecimentoId, pageable);
    }
}

