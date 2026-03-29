package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllServicosUseCase {

    private final ServicoRepository servicoRepository;

    public FindAllServicosUseCase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Page<Servico> findAll(Pageable pageable) {
        return servicoRepository.findAll(pageable);
    }
}

