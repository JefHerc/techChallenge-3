package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

import java.util.List;

public class CreateServicosNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final CreateServicoUseCase createServicoUseCase;

    public CreateServicosNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            CreateServicoUseCase createServicoUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.createServicoUseCase = createServicoUseCase;
    }

    public List<Servico> create(Long estabelecimentoId, List<Servico> servicos) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        return servicos.stream()
                .map(servico -> createServicoUseCase.create(estabelecimentoId, servico))
                .toList();
    }
}