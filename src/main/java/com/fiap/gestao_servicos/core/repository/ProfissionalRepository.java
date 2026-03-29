package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;

import java.util.Optional;

public interface ProfissionalRepository {

    Profissional create(Profissional profissional);
    Profissional create(Long estabelecimentoId, Profissional profissional);
    Profissional update(Long id, Profissional profissional);
    void deleteById(Long id);
    PageResult<Profissional> findAll(PageQuery pageQuery);
    PageResult<Profissional> findPageByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery);
    java.util.List<Profissional> findAllByEstabelecimentoId(Long estabelecimentoId);
    Optional<Profissional> findById(Long id);
    Optional<Profissional> findByIdAndEstabelecimentoId(Long id, Long estabelecimentoId);
    boolean existsById(Long id);
    boolean existsByIdAndEstabelecimentoId(Long id, Long estabelecimentoId);
    boolean existsByCpf(String cpf);
    boolean existsByCelular(String celular);
    boolean existsByEmail(String email);
    boolean existsVinculoProfissionalServico(Long profissionalId, Long servicoId);
}

