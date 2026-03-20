package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicoUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicoBeanConfig {

    @Bean
    public CreateServicoUsecase createServicoUsecase(ServicoRepository servicoRepository) {
        return new CreateServicoUsecase(servicoRepository);
    }

    @Bean
    public DeleteServicoUsecase deleteServicoUsecase(ServicoRepository servicoRepository) {
        return new DeleteServicoUsecase(servicoRepository);
    }

    @Bean
    public UpdateServicoUsecase updateServicoUsecase(ServicoRepository servicoRepository) {
        return new UpdateServicoUsecase(servicoRepository);
    }

    @Bean
    public FindAllServicosUsecase findAllServicosUsecase(ServicoRepository servicoRepository) {
        return new FindAllServicosUsecase(servicoRepository);
    }

    @Bean
    public FindServicoByIdUsecase findServicoByIdUsecase(ServicoRepository servicoRepository) {
        return new FindServicoByIdUsecase(servicoRepository);
    }
}