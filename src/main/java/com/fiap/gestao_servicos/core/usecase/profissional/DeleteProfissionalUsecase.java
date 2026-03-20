package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

public class DeleteProfissionalUsecase {

    private final ProfissionalRepository profissionalRepository;

    public DeleteProfissionalUsecase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public void deleteById(Long id) {
        if (!profissionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profissional não encontrado para o id: " + id);
        }
        profissionalRepository.deleteById(id);
    }
}