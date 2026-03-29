package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllAvaliacoesUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    public FindAllAvaliacoesUseCase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Page<Avaliacao> findAll(Pageable pageable) {
        return avaliacaoRepository.findAll(pageable);
    }

    public Page<Avaliacao> findByAgendamentoId(Long agendamentoId, Pageable pageable) {
        return avaliacaoRepository.findByAgendamentoId(agendamentoId, pageable);
    }
}


