package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class FindAllServicosUseCase {

    private final ServicoRepository servicoRepository;
    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;

    public FindAllServicosUseCase(ServicoRepository servicoRepository,
                                  FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        this.servicoRepository = servicoRepository;
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
    }

    public PageResult<Servico> findAll(PageQuery pageQuery) {
        return servicoRepository.findAll(pageQuery);
    }

    public PageResult<Servico> findPageByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        return servicoRepository.findPageByEstabelecimentoId(estabelecimentoId, pageQuery);
    }
}

