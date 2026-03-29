package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUseCase;
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
    public FindProfissionalByIdUseCase findProfissionalByIdUseCase(ProfissionalRepository profissionalRepository) {
        return new FindProfissionalByIdUseCase(profissionalRepository);
    }

    @Bean
    public FindAllProfissionaisUseCase findAllProfissionaisUseCase(ProfissionalRepository profissionalRepository) {
        return new FindAllProfissionaisUseCase(profissionalRepository);
    }

    @Bean
    public VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase() {
        return new VerifyProfissionalVinculoUseCase();
    }

    @Bean
    public CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase,
            VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase) {
        return new CreateProfissionalNoEstabelecimentoUseCase(
                findEstabelecimentoByIdUseCase,
                updateEstabelecimentoUseCase,
                verifyProfissionalVinculoUseCase);
    }
}
