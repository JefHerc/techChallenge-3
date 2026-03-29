package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;

public class CreateEstabelecimentoUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public CreateEstabelecimentoUseCase(EstabelecimentoRepository estabelecimentoRepository){
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento create(Estabelecimento estabelecimento){
        String cnpj = estabelecimento.getCnpj() != null ? estabelecimento.getCnpj().getNormalizedValue() : null;

        if (cnpj != null && estabelecimentoRepository.existsByCnpj(cnpj)) {
            throw new DuplicateDataException("Já existe um estabelecimento com o mesmo CNPJ.");
        }

        return estabelecimentoRepository.create(estabelecimento);
    }
}


