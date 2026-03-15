package com.fiap.gestao_servicos.core.usecase;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;

public class CreateEstabelecimentoUsecase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public CreateEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository){
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento create(Estabelecimento estabelecimento){
        String cnpj = estabelecimento.getCnpj() != null ? estabelecimento.getCnpj().getValue() : null;
        String nome = estabelecimento.getNome();

        if (cnpj != null && nome != null && estabelecimentoRepository.existsByCnpjAndNome(cnpj, nome)) {
            throw new DuplicateDataException("Já existe um estabelecimento com o mesmo CNPJ e nome.");
        }

        return estabelecimentoRepository.create(estabelecimento);
    }
}
