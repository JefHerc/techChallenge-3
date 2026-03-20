package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class UpdateEstabelecimentoUsecase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public UpdateEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento update(Long id, Estabelecimento estabelecimento) {
        Estabelecimento atual = estabelecimentoRepository.findById(id);
        if (atual == null) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado para o id: " + id);
        }

        String cnpjAtual = atual.getCnpj() != null ? atual.getCnpj().getValue() : null;
        String cnpjNovo = estabelecimento.getCnpj() != null ? estabelecimento.getCnpj().getValue() : null;
        String nomeAtual = atual.getNome();
        String nomeNovo = estabelecimento.getNome();

        boolean mudouIdentificacao = (cnpjAtual == null ? cnpjNovo != null : !cnpjAtual.equals(cnpjNovo))
                || (nomeAtual == null ? nomeNovo != null : !nomeAtual.equals(nomeNovo));

        if (cnpjNovo != null && nomeNovo != null && mudouIdentificacao
                && estabelecimentoRepository.existsByCnpjAndNome(cnpjNovo, nomeNovo)) {
            throw new DuplicateDataException("Já existe um estabelecimento com o mesmo CNPJ e nome.");
        }

        Estabelecimento atualizado = estabelecimentoRepository.update(id, estabelecimento);
        if (atualizado == null) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado para o id: " + id);
        }

        return atualizado;
    }
}