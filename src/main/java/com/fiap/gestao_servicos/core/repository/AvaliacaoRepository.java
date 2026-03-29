package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;

import java.util.Optional;

public interface AvaliacaoRepository {

    Avaliacao create(Avaliacao avaliacao);
    Avaliacao update(Long id, Avaliacao avaliacao);
    void deleteById(Long id);
    PageResult<Avaliacao> findAll(PageQuery pageQuery);
    PageResult<Avaliacao> findByAgendamentoId(Long agendamentoId, PageQuery pageQuery);
    PageResult<Avaliacao> findByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery);
    Optional<Avaliacao> findById(Long id);
    boolean existsById(Long id);
}

