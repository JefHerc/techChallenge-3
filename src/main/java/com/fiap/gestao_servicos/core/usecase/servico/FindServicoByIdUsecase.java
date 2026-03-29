package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class FindServicoByIdUseCase {

    private final ServicoRepository servicoRepository;
    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;

    public FindServicoByIdUseCase(ServicoRepository servicoRepository,
                                  FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        this.servicoRepository = servicoRepository;
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
    }

    public Servico findById(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Serviço",
                        id)));
    }

    public Servico findByIdAndEstabelecimentoId(Long id, Long estabelecimentoId) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        return servicoRepository.findByIdAndEstabelecimentoId(id, estabelecimentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Serviço não encontrado para o id: " + id + " no estabelecimento: " + estabelecimentoId
                ));
    }
}

