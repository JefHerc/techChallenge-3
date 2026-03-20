package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.CreateEstabelecimentoUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.DeleteEstabelecimentoUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindAllEstabelecimentosUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstabelecimentoBeanConfig {

    @Bean
    public CreateEstabelecimentoUsecase createEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        return new CreateEstabelecimentoUsecase(estabelecimentoRepository);
    }

    @Bean
    public DeleteEstabelecimentoUsecase deleteEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        return new DeleteEstabelecimentoUsecase(estabelecimentoRepository);
    }

    @Bean
    public UpdateEstabelecimentoUsecase updateEstabelecimentoUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        return new UpdateEstabelecimentoUsecase(estabelecimentoRepository);
    }

    @Bean
    public FindAllEstabelecimentosUsecase findAllEstabelecimentosUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        return new FindAllEstabelecimentosUsecase(estabelecimentoRepository);
    }

    @Bean
    public FindEstabelecimentoByIdUsecase findEstabelecimentoByIdUsecase(EstabelecimentoRepository estabelecimentoRepository) {
        return new FindEstabelecimentoByIdUsecase(estabelecimentoRepository);
    }
}
