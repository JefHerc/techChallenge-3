package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Agendamento;

import java.util.List;

public interface AgendamentoRepository {

    Agendamento create(Agendamento agendamento);
    Agendamento update(Long id, Agendamento agendamento);
    void deleteById(Long id);
    List<Agendamento> findAll();
    Agendamento findById(Long id);
    boolean existsById(Long id);
    boolean existsProfissionalById(Long id);
    boolean existsServicoById(Long id);
    boolean existsEstabelecimentoById(Long id);
    boolean existsClienteById(Long id);
}