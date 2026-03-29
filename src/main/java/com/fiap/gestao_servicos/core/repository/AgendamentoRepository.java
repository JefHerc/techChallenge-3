package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository {

    Agendamento create(Agendamento agendamento);
    Agendamento update(Long id, Agendamento agendamento);
    void deleteById(Long id);
    PageResult<Agendamento> findAll(PageQuery pageQuery);
    PageResult<Agendamento> findByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery);
    List<Agendamento> findByStatusAndDataHoraInicioBetween(AgendamentoStatus status,
                                                           LocalDateTime inicio,
                                                           LocalDateTime fim);
    Optional<Agendamento> findById(Long id);
    boolean existsById(Long id);
    boolean isConcluido(Long agendamentoId);
    boolean pertenceAoProfissionalEEstabelecimento(Long agendamentoId, Long profissionalId, Long estabelecimentoId);
    boolean existsConflitoHorarioProfissional(Long profissionalId,
                                              Long servicoId,
                                              LocalDateTime dataHoraInicio,
                                              Long agendamentoIdIgnorar);
}


