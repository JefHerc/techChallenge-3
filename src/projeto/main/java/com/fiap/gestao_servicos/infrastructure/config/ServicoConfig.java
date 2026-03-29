package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicoConfig {

    @Bean
    public CreateServicoUseCase createServicoUseCase(ServicoRepository servicoRepository) {
        return new CreateServicoUseCase(servicoRepository);
    }

    @Bean
    public UpdateServicoUseCase updateServicoUseCase(ServicoRepository servicoRepository) {
        return new UpdateServicoUseCase(servicoRepository);
    }

    @Bean
    public DeleteServicoUseCase deleteServicoUseCase(ServicoRepository servicoRepository) {
        return new DeleteServicoUseCase(servicoRepository);
    }

    @Bean
    public FindServicoByIdUseCase findServicoByIdUseCase(ServicoRepository servicoRepository) {
        return new FindServicoByIdUseCase(servicoRepository);
    }

    @Bean
    public FindAllServicosUseCase findAllServicosUseCase(ServicoRepository servicoRepository) {
        return new FindAllServicosUseCase(servicoRepository);
    }
}
