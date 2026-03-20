package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfissionalBeanConfig {

    @Bean
    public CreateProfissionalUsecase createProfissionalUsecase(ProfissionalRepository profissionalRepository) {
        return new CreateProfissionalUsecase(profissionalRepository);
    }

    @Bean
    public DeleteProfissionalUsecase deleteProfissionalUsecase(ProfissionalRepository profissionalRepository) {
        return new DeleteProfissionalUsecase(profissionalRepository);
    }

    @Bean
    public UpdateProfissionalUsecase updateProfissionalUsecase(ProfissionalRepository profissionalRepository) {
        return new UpdateProfissionalUsecase(profissionalRepository);
    }

    @Bean
    public FindAllProfissionaisUsecase findAllProfissionaisUsecase(ProfissionalRepository profissionalRepository) {
        return new FindAllProfissionaisUsecase(profissionalRepository);
    }

    @Bean
    public FindProfissionalByIdUsecase findProfissionalByIdUsecase(ProfissionalRepository profissionalRepository) {
        return new FindProfissionalByIdUsecase(profissionalRepository);
    }
}