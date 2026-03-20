package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

import java.util.List;

public class FindAllServicosUsecase {

    private final ServicoRepository servicoRepository;

    public FindAllServicosUsecase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public List<Servico> findAll() {
        return servicoRepository.findAll();
    }
}