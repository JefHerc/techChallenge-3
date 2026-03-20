package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Servico;

import java.util.List;

public interface ServicoRepository {

    Servico create(Servico servico);
    Servico update(Long id, Servico servico);
    void deleteById(Long id);
    List<Servico> findAll();
    Servico findById(Long id);
    boolean existsById(Long id);
    boolean existsByNomeIgnoreCase(String nome);
}