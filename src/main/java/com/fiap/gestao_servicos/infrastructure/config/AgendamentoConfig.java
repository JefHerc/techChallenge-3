package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.CreateAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.DeleteAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentosParaLembreteUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAllAgendamentosUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.UpdateAgendamentoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgendamentoConfig {

    @Bean
    public CreateAgendamentoUseCase createAgendamentoUseCase(
            AgendamentoRepository agendamentoRepository,
            EstabelecimentoRepository estabelecimentoRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            ClienteRepository clienteRepository) {
        return new CreateAgendamentoUseCase(
                agendamentoRepository,
                estabelecimentoRepository,
                servicoRepository,
                profissionalRepository,
                clienteRepository);
    }

    @Bean
    public UpdateAgendamentoUseCase updateAgendamentoUseCase(
            AgendamentoRepository agendamentoRepository,
            EstabelecimentoRepository estabelecimentoRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            ClienteRepository clienteRepository) {
        return new UpdateAgendamentoUseCase(
                agendamentoRepository,
                estabelecimentoRepository,
                servicoRepository,
                profissionalRepository,
                clienteRepository);
    }

    @Bean
    public DeleteAgendamentoUseCase deleteAgendamentoUseCase(AgendamentoRepository agendamentoRepository) {
        return new DeleteAgendamentoUseCase(agendamentoRepository);
    }

    @Bean
    public FindAgendamentoByIdUseCase findAgendamentoByIdUseCase(AgendamentoRepository agendamentoRepository) {
        return new FindAgendamentoByIdUseCase(agendamentoRepository);
    }

    @Bean
    public FindAllAgendamentosUseCase findAllAgendamentosUseCase(AgendamentoRepository agendamentoRepository) {
        return new FindAllAgendamentosUseCase(agendamentoRepository);
    }

    @Bean
    public FindAgendamentosParaLembreteUseCase findAgendamentosParaLembreteUseCase(AgendamentoRepository agendamentoRepository) {
        return new FindAgendamentosParaLembreteUseCase(agendamentoRepository);
    }
}
