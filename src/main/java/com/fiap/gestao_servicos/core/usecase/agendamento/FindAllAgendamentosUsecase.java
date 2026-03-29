package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class FindAllAgendamentosUseCase {

    private final AgendamentoRepository agendamentoRepository;

    public FindAllAgendamentosUseCase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public PageResult<Agendamento> findAll(PageQuery pageQuery) {
        return agendamentoRepository.findAll(pageQuery);
    }

    public PageResult<Agendamento> findByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        if (estabelecimentoId == null || estabelecimentoId <= 0) {
            throw new IllegalArgumentException("estabelecimentoId deve ser informado com um valor positivo");
        }
        return agendamentoRepository.findByEstabelecimentoId(estabelecimentoId, pageQuery);
    }
}

