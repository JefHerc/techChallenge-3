package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EstabelecimentoRepository {

    Estabelecimento create(Estabelecimento estabelecimento);
    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndNome(String cnpj, String nome);
    boolean existsByCnpjAndIdNot(String cnpj, Long id);
    Estabelecimento update(Long id, Estabelecimento estabelecimento);
    Estabelecimento updateDadosCadastrais(Long id, Estabelecimento estabelecimento);
    void deleteById(Long id);
    Page<Estabelecimento> findAll(Pageable pageable);
    Optional<Estabelecimento> findById(Long id);
    boolean existsById(Long id);
    boolean temProfissional(Long estabelecimentoId, Long profissionalId);
    Page<Estabelecimento> findByCriteria(EstabelecimentoFilter filter, Pageable pageable);
}


