package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

import java.util.List;

public class FindAllProfissionaisUsecase {

    private final ProfissionalRepository profissionalRepository;

    public FindAllProfissionaisUsecase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public List<Profissional> findAll() {
        return profissionalRepository.findAll();
    }
}