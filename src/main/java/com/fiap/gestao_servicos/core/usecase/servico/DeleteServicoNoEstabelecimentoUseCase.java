package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class DeleteServicoNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final ServicoRepository servicoRepository;
    private final DeleteServicoUseCase deleteServicoUseCase;

    public DeleteServicoNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ServicoRepository servicoRepository,
            DeleteServicoUseCase deleteServicoUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.servicoRepository = servicoRepository;
        this.deleteServicoUseCase = deleteServicoUseCase;
    }

    public void deleteById(Long estabelecimentoId, Long servicoId) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        if (!servicoRepository.existsByIdAndEstabelecimentoId(servicoId, estabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Serviço não encontrado para o id: " + servicoId + " no estabelecimento: " + estabelecimentoId
            );
        }
        deleteServicoUseCase.deleteById(servicoId);
    }
}