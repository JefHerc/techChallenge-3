package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class FindEstabelecimentoByIdUsecase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public FindEstabelecimentoByIdUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento findById(Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id);
        if (estabelecimento == null) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado para o id: " + id);
        }
        return estabelecimento;
    }
}