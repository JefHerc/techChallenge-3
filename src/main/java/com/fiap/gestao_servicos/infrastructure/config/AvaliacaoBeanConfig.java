package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.core.usecase.avaliacao.CreateAvaliacaoUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.DeleteAvaliacaoUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAllAvaliacoesUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAvaliacaoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.UpdateAvaliacaoUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvaliacaoBeanConfig {

    @Bean
    public CreateAvaliacaoUsecase createAvaliacaoUsecase(AvaliacaoRepository avaliacaoRepository) {
        return new CreateAvaliacaoUsecase(avaliacaoRepository);
    }

    @Bean
    public DeleteAvaliacaoUsecase deleteAvaliacaoUsecase(AvaliacaoRepository avaliacaoRepository) {
        return new DeleteAvaliacaoUsecase(avaliacaoRepository);
    }

    @Bean
    public UpdateAvaliacaoUsecase updateAvaliacaoUsecase(AvaliacaoRepository avaliacaoRepository) {
        return new UpdateAvaliacaoUsecase(avaliacaoRepository);
    }

    @Bean
    public FindAllAvaliacoesUsecase findAllAvaliacoesUsecase(AvaliacaoRepository avaliacaoRepository) {
        return new FindAllAvaliacoesUsecase(avaliacaoRepository);
    }

    @Bean
    public FindAvaliacaoByIdUsecase findAvaliacaoByIdUsecase(AvaliacaoRepository avaliacaoRepository) {
        return new FindAvaliacaoByIdUsecase(avaliacaoRepository);
    }
}
