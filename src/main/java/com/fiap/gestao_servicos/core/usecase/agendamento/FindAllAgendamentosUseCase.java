package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FindAllAgendamentosUseCase {

    private final AgendamentoRepository agendamentoRepository;
    private final Clock clock;

    public FindAllAgendamentosUseCase(AgendamentoRepository agendamentoRepository) {
        this(agendamentoRepository, Clock.systemDefaultZone());
    }

    public FindAllAgendamentosUseCase(AgendamentoRepository agendamentoRepository, Clock clock) {
        this.agendamentoRepository = agendamentoRepository;
        this.clock = clock;
    }

    public PageResult<Agendamento> findAll(PageQuery pageQuery) {
        return agendamentoRepository.findAll(pageQuery);
    }

    public PageResult<Agendamento> findByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        validateEstabelecimentoId(estabelecimentoId);
        return agendamentoRepository.findByEstabelecimentoId(estabelecimentoId, pageQuery);
    }

    public List<Agendamento> findByEstabelecimentoIdAndPeriodo(Long estabelecimentoId,
                                                               LocalDate dataInicial,
                                                               LocalDate dataFinal) {
        validateEstabelecimentoId(estabelecimentoId);

        LocalDate inicio = dataInicial != null ? dataInicial : LocalDate.now(clock);
        LocalDate fim = dataFinal != null ? dataFinal : inicio.plusDays(30);

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("dataFinal deve ser maior ou igual à dataInicial");
        }

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.plusDays(1).atStartOfDay().minusNanos(1);

        return agendamentoRepository.findByEstabelecimentoIdAndDataHoraInicioBetween(
                estabelecimentoId,
                inicioDateTime,
                fimDateTime);
    }

    private void validateEstabelecimentoId(Long estabelecimentoId) {
        if (estabelecimentoId == null || estabelecimentoId <= 0) {
            throw new IllegalArgumentException("estabelecimentoId deve ser informado com um valor positivo");
        }
    }
}

