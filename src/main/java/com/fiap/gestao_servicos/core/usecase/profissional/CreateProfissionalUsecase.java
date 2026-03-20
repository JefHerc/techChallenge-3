package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

public class CreateProfissionalUsecase {

    private final ProfissionalRepository profissionalRepository;

    public CreateProfissionalUsecase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public Profissional create(Profissional profissional) {
        validarDuplicidade(profissional);
        return profissionalRepository.create(profissional);
    }

    private void validarDuplicidade(Profissional profissional) {
        if (profissionalRepository.existsByCpf(profissional.getCpf().getValue())) {
            throw new DuplicateDataException("Já existe um profissional com o mesmo CPF.");
        }
        if (profissionalRepository.existsByCelular(profissional.getCelular().getValue())) {
            throw new DuplicateDataException("Já existe um profissional com o mesmo celular.");
        }
        if (profissionalRepository.existsByEmail(profissional.getEmail().getValue())) {
            throw new DuplicateDataException("Já existe um profissional com o mesmo email.");
        }
    }
}