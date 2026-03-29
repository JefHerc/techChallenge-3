package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class DeleteProfissionalNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final ProfissionalRepository profissionalRepository;
    private final DeleteProfissionalUseCase deleteProfissionalUseCase;

    public DeleteProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ProfissionalRepository profissionalRepository,
            DeleteProfissionalUseCase deleteProfissionalUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.profissionalRepository = profissionalRepository;
        this.deleteProfissionalUseCase = deleteProfissionalUseCase;
    }

    public void deleteById(Long estabelecimentoId, Long profissionalId) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        if (!profissionalRepository.existsByIdAndEstabelecimentoId(profissionalId, estabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Profissional nao encontrado para o id: " + profissionalId + " no estabelecimento: " + estabelecimentoId
            );
        }
        deleteProfissionalUseCase.deleteById(profissionalId);
    }
}