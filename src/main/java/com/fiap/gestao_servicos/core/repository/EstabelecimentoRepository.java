package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;

import java.util.List;

public interface EstabelecimentoRepository {

    Estabelecimento create(Estabelecimento estabelecimento);
    boolean existsByCnpjAndNome(String cnpj, String nome);
    Estabelecimento update(Estabelecimento estabelecimento);
    void delete(Estabelecimento estabelecimento);
    List<Estabelecimento> findAll();
    Estabelecimento findById(Long id);
}
