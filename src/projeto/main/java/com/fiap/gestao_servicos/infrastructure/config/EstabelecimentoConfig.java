package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.CreateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.DeleteEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindAllEstabelecimentosUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentosByCriteriaUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstabelecimentoConfig {

    @Bean
    public CreateEstabelecimentoUseCase createEstabelecimentoUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        return new CreateEstabelecimentoUseCase(estabelecimentoRepository);
    }

    @Bean
    public UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        return new UpdateEstabelecimentoUseCase(estabelecimentoRepository);
    }

    @Bean
    public DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        return new DeleteEstabelecimentoUseCase(estabelecimentoRepository);
    }

    @Bean
    public FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        return new FindEstabelecimentoByIdUseCase(estabelecimentoRepository);
    }

    @Bean
    public FindAllEstabelecimentosUseCase findAllEstabelecimentosUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        return new FindAllEstabelecimentosUseCase(estabelecimentoRepository);
    }

    @Bean
    public FindEstabelecimentosByCriteriaUseCase findEstabelecimentosByCriteriaUseCase(EstabelecimentoRepository estabelecimentoRepository) {
        return new FindEstabelecimentosByCriteriaUseCase(estabelecimentoRepository);
    }
}
