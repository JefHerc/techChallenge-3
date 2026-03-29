package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServicoRepository {

    Servico create(Servico servico);
    Servico update(Long id, Servico servico);
    void deleteById(Long id);
    Page<Servico> findAll(Pageable pageable);
    Optional<Servico> findById(Long id);
    boolean existsById(Long id);
    boolean existsByNomeIgnoreCase(String nome);
}

