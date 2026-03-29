package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;

import java.util.Optional;

public interface ServicoRepository {

    Servico create(Servico servico);
    Servico create(Long estabelecimentoId, Servico servico);
    Servico update(Long id, Servico servico);
    void deleteById(Long id);
    PageResult<Servico> findAll(PageQuery pageQuery);
    PageResult<Servico> findPageByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery);
    java.util.List<Servico> findAllByEstabelecimentoId(Long estabelecimentoId);
    Optional<Servico> findById(Long id);
    Optional<Servico> findByIdAndEstabelecimentoId(Long id, Long estabelecimentoId);
    boolean existsById(Long id);
    boolean existsByIdAndEstabelecimentoId(Long id, Long estabelecimentoId);
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndEstabelecimentoId(String nome, Long estabelecimentoId);
}

