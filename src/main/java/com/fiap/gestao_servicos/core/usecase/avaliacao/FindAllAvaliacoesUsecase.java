package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;

import java.util.List;

public class FindAllAvaliacoesUsecase {

    private final AvaliacaoRepository avaliacaoRepository;

    public FindAllAvaliacoesUsecase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public List<Avaliacao> findAll() {
        return avaliacaoRepository.findAll();
    }
}
