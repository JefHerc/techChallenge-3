package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;

import java.util.List;

public class UpdateProfissionalNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase;
    private final UpdateProfissionalUseCase updateProfissionalUseCase;

    public UpdateProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ProfissionalRepository profissionalRepository,
            ServicoRepository servicoRepository,
            VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase,
            UpdateProfissionalUseCase updateProfissionalUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
        this.verifyProfissionalVinculoUseCase = verifyProfissionalVinculoUseCase;
        this.updateProfissionalUseCase = updateProfissionalUseCase;
    }

    public Profissional update(Long estabelecimentoId, Long id, Profissional profissionalBase) {
        validarProfissionalPertenceAoEstabelecimento(estabelecimentoId, id);
        Estabelecimento estabelecimento = buildEstabelecimentoParaVinculo(estabelecimentoId);

        Profissional profissional = new Profissional(
                id,
                profissionalBase.getNome(),
                profissionalBase.getCpf(),
                profissionalBase.getCelular(),
                profissionalBase.getEmail(),
                profissionalBase.getUrlFoto(),
                profissionalBase.getDescricao(),
                profissionalBase.getExpedientes(),
                profissionalBase.getSexo(),
                profissionalBase.getServicosProfissional()
        );

        verifyProfissionalVinculoUseCase.verify(estabelecimento, profissional);
        return updateProfissionalUseCase.update(id, profissional);
    }

    private void validarProfissionalPertenceAoEstabelecimento(Long estabelecimentoId, Long profissionalId) {
        findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        if (!profissionalRepository.existsByIdAndEstabelecimentoId(profissionalId, estabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Profissional nao encontrado para o id: " + profissionalId + " no estabelecimento: " + estabelecimentoId
            );
        }
    }

    private Estabelecimento buildEstabelecimentoParaVinculo(Long estabelecimentoId) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        List<Servico> servicos = servicoRepository.findAllByEstabelecimentoId(estabelecimentoId);
        return new Estabelecimento(
                estabelecimento.getId(),
                estabelecimento.getNome(),
                estabelecimento.getEndereco(),
                null,
                servicos,
                estabelecimento.getCnpj(),
                estabelecimento.getUrlFotos(),
                estabelecimento.getHorarioFuncionamento()
        );
    }
}