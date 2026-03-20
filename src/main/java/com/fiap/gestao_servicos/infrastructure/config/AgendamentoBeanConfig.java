package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.CreateAgendamentoUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.DeleteAgendamentoUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAllAgendamentosUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.UpdateAgendamentoUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgendamentoBeanConfig {

    @Bean
    public CreateAgendamentoUsecase createAgendamentoUsecase(AgendamentoRepository agendamentoRepository) {
        return new CreateAgendamentoUsecase(agendamentoRepository);
    }

    @Bean
    public DeleteAgendamentoUsecase deleteAgendamentoUsecase(AgendamentoRepository agendamentoRepository) {
        return new DeleteAgendamentoUsecase(agendamentoRepository);
    }

    @Bean
    public UpdateAgendamentoUsecase updateAgendamentoUsecase(AgendamentoRepository agendamentoRepository) {
        return new UpdateAgendamentoUsecase(agendamentoRepository);
    }

    @Bean
    public FindAllAgendamentosUsecase findAllAgendamentosUsecase(AgendamentoRepository agendamentoRepository) {
        return new FindAllAgendamentosUsecase(agendamentoRepository);
    }

    @Bean
    public FindAgendamentoByIdUsecase findAgendamentoByIdUsecase(AgendamentoRepository agendamentoRepository) {
        return new FindAgendamentoByIdUsecase(agendamentoRepository);
    }
}