package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

public class FindProfissionalByIdUseCase {

    private final ProfissionalRepository profissionalRepository;
    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;

    public FindProfissionalByIdUseCase(ProfissionalRepository profissionalRepository,
                                       FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        this.profissionalRepository = profissionalRepository;
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
    }

    public Profissional findById(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Profissional",
                        id)));
    }

    public Profissional findByIdAndEstabelecimentoId(Long id, Long estabelecimentoId) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        return profissionalRepository.findByIdAndEstabelecimentoId(id, estabelecimentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profissional nao encontrado para o id: " + id + " no estabelecimento: " + estabelecimentoId
                ));
    }
}

