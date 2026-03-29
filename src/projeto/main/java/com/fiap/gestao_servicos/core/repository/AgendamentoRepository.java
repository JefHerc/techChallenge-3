package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository {

    Agendamento create(Agendamento agendamento);
    Agendamento update(Long id, Agendamento agendamento);
    void deleteById(Long id);
    Page<Agendamento> findAll(Pageable pageable);
    Page<Agendamento> findByEstabelecimentoId(Long estabelecimentoId, Pageable pageable);
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


