package com.fiap.gestao_servicos.core.usecase.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;

public class FindServicoByIdUsecase {

    private final ServicoRepository servicoRepository;

    public FindServicoByIdUsecase(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico findById(Long id) {
        Servico servico = servicoRepository.findById(id);
        if (servico == null) {
            throw new ResourceNotFoundException("Serviço não encontrado para o id: " + id);
        }
        return servico;
    }
}