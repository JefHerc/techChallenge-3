package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.usecase.avaliacao.CreateAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.DeleteAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAllAvaliacoesUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAvaliacaoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.UpdateAvaliacaoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvaliacaoConfig {

    @Bean
    public CreateAvaliacaoUseCase createAvaliacaoUseCase(
            AvaliacaoRepository avaliacaoRepository,
            AgendamentoRepository agendamentoRepository,
            ProfissionalRepository profissionalRepository,
            EstabelecimentoRepository estabelecimentoRepository) {
        return new CreateAvaliacaoUseCase(
                avaliacaoRepository,
                agendamentoRepository,
                profissionalRepository,
                estabelecimentoRepository);
    }

    @Bean
    public UpdateAvaliacaoUseCase updateAvaliacaoUseCase(
            AvaliacaoRepository avaliacaoRepository,
            AgendamentoRepository agendamentoRepository,
            ProfissionalRepository profissionalRepository,
            EstabelecimentoRepository estabelecimentoRepository) {
        return new UpdateAvaliacaoUseCase(
                avaliacaoRepository,
                agendamentoRepository,
                profissionalRepository,
                estabelecimentoRepository);
    }

    @Bean
    public DeleteAvaliacaoUseCase deleteAvaliacaoUseCase(AvaliacaoRepository avaliacaoRepository) {
        return new DeleteAvaliacaoUseCase(avaliacaoRepository);
    }

    @Bean
    public FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase(AvaliacaoRepository avaliacaoRepository) {
        return new FindAvaliacaoByIdUseCase(avaliacaoRepository);
    }

    @Bean
    public FindAllAvaliacoesUseCase findAllAvaliacoesUseCase(AvaliacaoRepository avaliacaoRepository) {
        return new FindAllAvaliacoesUseCase(avaliacaoRepository);
    }
}
