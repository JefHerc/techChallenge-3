package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

public class FindProfissionalByIdUseCase {

    private final ProfissionalRepository profissionalRepository;

    public FindProfissionalByIdUseCase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public Profissional findById(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Profissional",
                        id)));
    }
}

