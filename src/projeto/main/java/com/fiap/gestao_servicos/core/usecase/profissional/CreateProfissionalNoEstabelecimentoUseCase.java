package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateProfissionalNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase;
    private final VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase;

    public CreateProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase,
            VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase
    ) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.updateEstabelecimentoUseCase = updateEstabelecimentoUseCase;
        this.verifyProfissionalVinculoUseCase = verifyProfissionalVinculoUseCase;
    }

    public Profissional create(Long estabelecimentoId, Profissional novoProfissional) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        verifyProfissionalVinculoUseCase.verify(estabelecimento, novoProfissional);

        List<Profissional> profissionaisAtualizados = new ArrayList<>(estabelecimento.getProfissionais());
        profissionaisAtualizados.add(novoProfissional);

        Estabelecimento atualizado = updateEstabelecimentoUseCase.update(
                estabelecimentoId,
                copyWithProfissionais(estabelecimento, profissionaisAtualizados)
        );

        return atualizado.getProfissionais().stream()
                .filter(profissional -> isMesmoProfissional(profissional, novoProfissional))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profissional criado nao encontrado no estabelecimento: " + estabelecimentoId
                ));
    }

    private boolean isMesmoProfissional(Profissional atual, Profissional novo) {
        String cpfAtual = atual.getCpf() != null ? atual.getCpf().getValue() : null;
        String cpfNovo = novo.getCpf() != null ? novo.getCpf().getValue() : null;
        if (Objects.equals(cpfAtual, cpfNovo) && cpfAtual != null) {
            return true;
        }

        String emailAtual = atual.getEmail() != null ? atual.getEmail().getValue() : null;
        String emailNovo = novo.getEmail() != null ? novo.getEmail().getValue() : null;
        String celularAtual = atual.getCelular() != null ? atual.getCelular().getValue() : null;
        String celularNovo = novo.getCelular() != null ? novo.getCelular().getValue() : null;
        return Objects.equals(emailAtual, emailNovo)
                && Objects.equals(celularAtual, celularNovo)
                && emailAtual != null;
    }

    private Estabelecimento copyWithProfissionais(Estabelecimento origem, List<Profissional> profissionais) {
        return new Estabelecimento(
                origem.getId(),
                origem.getNome(),
                origem.getEndereco(),
                profissionais,
                origem.getServicos(),
                origem.getCnpj(),
                origem.getUrlFotos(),
                origem.getHorarioFuncionamento()
        );
    }
}