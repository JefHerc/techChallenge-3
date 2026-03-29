package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.usecase.cliente.CreateClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.DeleteClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindAllClientesUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindClienteByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.UpdateClienteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteConfig {

    @Bean
    public CreateClienteUseCase createClienteUseCase(ClienteRepository clienteRepository) {
        return new CreateClienteUseCase(clienteRepository);
    }

    @Bean
    public UpdateClienteUseCase updateClienteUseCase(ClienteRepository clienteRepository) {
        return new UpdateClienteUseCase(clienteRepository);
    }

    @Bean
    public DeleteClienteUseCase deleteClienteUseCase(ClienteRepository clienteRepository) {
        return new DeleteClienteUseCase(clienteRepository);
    }

    @Bean
    public FindClienteByIdUseCase findClienteByIdUseCase(ClienteRepository clienteRepository) {
        return new FindClienteByIdUseCase(clienteRepository);
    }

    @Bean
    public FindAllClientesUseCase findAllClientesUseCase(ClienteRepository clienteRepository) {
        return new FindAllClientesUseCase(clienteRepository);
    }
}
