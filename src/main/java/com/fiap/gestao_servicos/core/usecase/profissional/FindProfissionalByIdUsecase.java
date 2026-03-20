package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

public class FindProfissionalByIdUsecase {

    private final ProfissionalRepository profissionalRepository;

    public FindProfissionalByIdUsecase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public Profissional findById(Long id) {
        Profissional profissional = profissionalRepository.findById(id);
        if (profissional == null) {
            throw new ResourceNotFoundException("Profissional não encontrado para o id: " + id);
        }
        return profissional;
    }
}