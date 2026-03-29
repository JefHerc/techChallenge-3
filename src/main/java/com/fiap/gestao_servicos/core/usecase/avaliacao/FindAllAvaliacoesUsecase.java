package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;

public class FindAllAvaliacoesUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    public FindAllAvaliacoesUseCase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public PageResult<Avaliacao> findAll(PageQuery pageQuery) {
        return avaliacaoRepository.findAll(pageQuery);
    }

    public PageResult<Avaliacao> findByAgendamentoId(Long agendamentoId, PageQuery pageQuery) {
        return avaliacaoRepository.findByAgendamentoId(agendamentoId, pageQuery);
    }

    public PageResult<Avaliacao> findByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        return avaliacaoRepository.findByEstabelecimentoId(estabelecimentoId, pageQuery);
    }
}


