package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AvaliacaoRepository {

    Avaliacao create(Avaliacao avaliacao);
    Avaliacao update(Long id, Avaliacao avaliacao);
    void deleteById(Long id);
    Page<Avaliacao> findAll(Pageable pageable);
    Page<Avaliacao> findByAgendamentoId(Long agendamentoId, Pageable pageable);
    Optional<Avaliacao> findById(Long id);
    boolean existsById(Long id);
}

