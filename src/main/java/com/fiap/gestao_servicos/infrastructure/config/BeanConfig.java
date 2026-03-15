package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.usecase.CreateEstabelecimentoUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateEstabelecimentoUsecase createEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        return new CreateEstabelecimentoUsecase(estabelecimentoRepository);
    }
}
