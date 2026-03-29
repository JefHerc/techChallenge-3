package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicosNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoNoEstabelecimentoUseCase;
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
    public FindServicoByIdUseCase findServicoByIdUseCase(
            ServicoRepository servicoRepository,
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        return new FindServicoByIdUseCase(servicoRepository, findEstabelecimentoByIdUseCase);
    }

    @Bean
    public FindAllServicosUseCase findAllServicosUseCase(
            ServicoRepository servicoRepository,
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase) {
        return new FindAllServicosUseCase(servicoRepository, findEstabelecimentoByIdUseCase);
    }

    @Bean
    public CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            CreateServicoUseCase createServicoUseCase) {
        return new CreateServicosNoEstabelecimentoUseCase(findEstabelecimentoByIdUseCase, createServicoUseCase);
    }

    @Bean
    public UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ServicoRepository servicoRepository,
            UpdateServicoUseCase updateServicoUseCase) {
        return new UpdateServicoNoEstabelecimentoUseCase(
                findEstabelecimentoByIdUseCase,
                servicoRepository,
                updateServicoUseCase);
    }

    @Bean
    public DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase(
            FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
            ServicoRepository servicoRepository,
            DeleteServicoUseCase deleteServicoUseCase) {
        return new DeleteServicoNoEstabelecimentoUseCase(
                findEstabelecimentoByIdUseCase,
                servicoRepository,
                deleteServicoUseCase);
    }
}
