package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import java.util.List;

public class CreateProfissionalNoEstabelecimentoUseCase {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final ServicoRepository servicoRepository;
    private final CreateProfissionalUseCase createProfissionalUseCase;
    private final VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase;

    public CreateProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
        ServicoRepository servicoRepository,
        CreateProfissionalUseCase createProfissionalUseCase,
            VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase
    ) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
    this.servicoRepository = servicoRepository;
    this.createProfissionalUseCase = createProfissionalUseCase;
        this.verifyProfissionalVinculoUseCase = verifyProfissionalVinculoUseCase;
    }

    public Profissional create(Long estabelecimentoId, Profissional novoProfissional) {
    Estabelecimento estabelecimentoBase = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
    List<Servico> servicos = servicoRepository.findAllByEstabelecimentoId(estabelecimentoId);
    Estabelecimento estabelecimento = new Estabelecimento(
        estabelecimentoBase.getId(),
        estabelecimentoBase.getNome(),
        estabelecimentoBase.getEndereco(),
        null,
        servicos,
        estabelecimentoBase.getCnpj(),
        estabelecimentoBase.getUrlFotos(),
        estabelecimentoBase.getHorarioFuncionamento()
    );
    verifyProfissionalVinculoUseCase.verify(estabelecimento, novoProfissional);
    return createProfissionalUseCase.create(estabelecimentoId, novoProfissional);
    }
}