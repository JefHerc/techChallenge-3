package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllProfissionaisUseCase {

    private final ProfissionalRepository profissionalRepository;

    public FindAllProfissionaisUseCase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public Page<Profissional> findAll(Pageable pageable) {
        return profissionalRepository.findAll(pageable);
    }
}

