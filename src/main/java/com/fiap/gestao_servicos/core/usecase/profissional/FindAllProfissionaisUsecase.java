package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class FindAllProfissionaisUseCase {

    private final ProfissionalRepository profissionalRepository;
    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;

    public FindAllProfissionaisUseCase(ProfissionalRepository profissionalRepository,
                                       FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        this.profissionalRepository = profissionalRepository;
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
    }

    public PageResult<Profissional> findAll(PageQuery pageQuery) {
        return profissionalRepository.findAll(pageQuery);
    }

    public PageResult<Profissional> findPageByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        return profissionalRepository.findPageByEstabelecimentoId(estabelecimentoId, pageQuery);
    }
}

