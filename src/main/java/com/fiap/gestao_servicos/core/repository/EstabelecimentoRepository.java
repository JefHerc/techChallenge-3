package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoSearchResult;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;

import java.util.Optional;

public interface EstabelecimentoRepository {

    Estabelecimento create(Estabelecimento estabelecimento);
    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndNome(String cnpj, String nome);
    boolean existsByCnpjAndIdNot(String cnpj, Long id);
    Estabelecimento update(Long id, Estabelecimento estabelecimento);
    Estabelecimento updateDadosCadastrais(Long id, Estabelecimento estabelecimento);
    void deleteById(Long id);
    PageResult<Estabelecimento> findAll(PageQuery pageQuery);
    Optional<Estabelecimento> findById(Long id);
    boolean existsById(Long id);
    boolean temProfissional(Long estabelecimentoId, Long profissionalId);
    PageResult<Estabelecimento> findByCriteria(EstabelecimentoFilter filter, PageQuery pageQuery);
    PageResult<EstabelecimentoSearchResult> findByCriteriaWithServices(EstabelecimentoFilter filter, PageQuery pageQuery);
}


