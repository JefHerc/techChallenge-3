package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class UpdateServicoNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final ServicoRepository servicoRepository;
    private final UpdateServicoUseCase updateServicoUseCase;

    public UpdateServicoNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ServicoRepository servicoRepository,
            UpdateServicoUseCase updateServicoUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.servicoRepository = servicoRepository;
        this.updateServicoUseCase = updateServicoUseCase;
    }

    public Servico update(Long estabelecimentoId, Long id, Servico servicoBase) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        if (!servicoRepository.existsByIdAndEstabelecimentoId(id, estabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Serviço não encontrado para o id: " + id + " no estabelecimento: " + estabelecimentoId
            );
        }

        Servico servico = new Servico(id, servicoBase.getNome(), servicoBase.getDuracaoMedia());
        return updateServicoUseCase.update(id, servico);
    }
}