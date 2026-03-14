package com.fiap.gestao_servicos.core.domain;

import java.util.List;

public interface EstabelecimentoRepository {

    Estabelecimento create(Estabelecimento estabelecimento);
    Estabelecimento update(Estabelecimento estabelecimento);
    void delete(Estabelecimento estabelecimento);
    List<Estabelecimento> findAll();
    Estabelecimento findById(Long id);
}
