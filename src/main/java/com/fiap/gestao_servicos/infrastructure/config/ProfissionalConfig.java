package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.VerifyProfissionalVinculoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfissionalConfig {

    @Bean
    public CreateProfissionalUseCase createProfissionalUseCase(ProfissionalRepository profissionalRepository) {
        return new CreateProfissionalUseCase(profissionalRepository);
    }

    @Bean
    public UpdateProfissionalUseCase updateProfissionalUseCase(ProfissionalRepository profissionalRepository) {
        return new UpdateProfissionalUseCase(profissionalRepository);
    }

    @Bean
    public DeleteProfissionalUseCase deleteProfissionalUseCase(ProfissionalRepository profissionalRepository) {
        return new DeleteProfissionalUseCase(profissionalRepository);
    }

    @Bean
    public FindProfissionalByIdUseCase findProfissionalByIdUseCase(
            ProfissionalRepository profissionalRepository,
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        return new FindProfissionalByIdUseCase(profissionalRepository, findEstabelecimentoByIdUseCase);
    }

    @Bean
    public FindAllProfissionaisUseCase findAllProfissionaisUseCase(
            ProfissionalRepository profissionalRepository,
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        return new FindAllProfissionaisUseCase(profissionalRepository, findEstabelecimentoByIdUseCase);
    }

    @Bean
    public VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase() {
        return new VerifyProfissionalVinculoUseCase();
    }

    @Bean
    public CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ServicoRepository servicoRepository,
            CreateProfissionalUseCase createProfissionalUseCase,
            VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase) {
        return new CreateProfissionalNoEstabelecimentoUseCase(
                findEstabelecimentoByIdUseCase,
                servicoRepository,
                createProfissionalUseCase,
                verifyProfissionalVinculoUseCase);
    }

    @Bean
    public UpdateProfissionalNoEstabelecimentoUseCase updateProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ProfissionalRepository profissionalRepository,
            ServicoRepository servicoRepository,
            VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase,
            UpdateProfissionalUseCase updateProfissionalUseCase) {
        return new UpdateProfissionalNoEstabelecimentoUseCase(
                findEstabelecimentoByIdUseCase,
                profissionalRepository,
                servicoRepository,
                verifyProfissionalVinculoUseCase,
                updateProfissionalUseCase);
    }

    @Bean
    public DeleteProfissionalNoEstabelecimentoUseCase deleteProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ProfissionalRepository profissionalRepository,
            DeleteProfissionalUseCase deleteProfissionalUseCase) {
        return new DeleteProfissionalNoEstabelecimentoUseCase(
                findEstabelecimentoByIdUseCase,
                profissionalRepository,
                deleteProfissionalUseCase);
    }
}
