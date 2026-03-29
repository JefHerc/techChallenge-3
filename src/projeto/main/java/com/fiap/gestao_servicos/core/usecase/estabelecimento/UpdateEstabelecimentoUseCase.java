package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;

public class UpdateEstabelecimentoUseCase {

    private final EstabelecimentoRepository estabelecimentoRepository;

    public UpdateEstabelecimentoUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Estabelecimento update(Long id, Estabelecimento estabelecimento) {
        return updateInterno(id, estabelecimento, false);
    }

    public Estabelecimento updateDadosCadastrais(Long id, Estabelecimento estabelecimento) {
        return updateInterno(id, estabelecimento, true);
    }

    private Estabelecimento updateInterno(Long id, Estabelecimento estabelecimento, boolean apenasDadosCadastrais) {
        Estabelecimento atual = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Estabelecimento",
                        id)));

        String cnpjAtual = atual.getCnpj() != null ? atual.getCnpj().getValue() : null;
        String cnpjNovo = estabelecimento.getCnpj() != null ? estabelecimento.getCnpj().getValue() : null;
        String nomeAtual = atual.getNome();
        String nomeNovo = estabelecimento.getNome();

        boolean mudouIdentificacao = (cnpjAtual == null ? cnpjNovo != null : !cnpjAtual.equals(cnpjNovo))
                || (nomeAtual == null ? nomeNovo != null : !nomeAtual.equals(nomeNovo));

        if (cnpjNovo != null && nomeNovo != null && mudouIdentificacao
                && estabelecimentoRepository.existsByCnpjAndNome(cnpjNovo, nomeNovo)) {
            throw new DuplicateDataException("Ja existe um estabelecimento com o mesmo CNPJ e nome.");
        }

        if (cnpjNovo != null && estabelecimentoRepository.existsByCnpjAndIdNot(cnpjNovo, id)) {
            throw new DuplicateDataException("Ja existe um estabelecimento com o mesmo CNPJ.");
        }

        Estabelecimento atualizado = apenasDadosCadastrais
                ? estabelecimentoRepository.updateDadosCadastrais(id, estabelecimento)
                : estabelecimentoRepository.update(id, estabelecimento);
        if (atualizado == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Estabelecimento", id));
        }

        return atualizado;
    }
}


