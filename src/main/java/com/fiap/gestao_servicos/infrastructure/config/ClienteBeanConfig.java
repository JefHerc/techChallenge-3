package com.fiap.gestao_servicos.infrastructure.config;

import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.usecase.cliente.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteBeanConfig {

    @Bean
    public CreateClienteUsecase createClienteUsecase(ClienteRepository clienteRepository) {
        return new CreateClienteUsecase(clienteRepository);
    }

    @Bean
    public DeleteClienteUsecase deleteClienteUsecase(ClienteRepository clienteRepository) {
        return new DeleteClienteUsecase(clienteRepository);
    }

    @Bean
    public UpdateClienteUsecase updateClienteUsecase(ClienteRepository clienteRepository) {
        return new UpdateClienteUsecase(clienteRepository);
    }

    @Bean
    public FindAllClientesUsecase findAllClientesUsecase(ClienteRepository clienteRepository) {
        return new FindAllClientesUsecase(clienteRepository);
    }

    @Bean
    public FindClienteByIdUsecase findClienteByIdUsecase(ClienteRepository clienteRepository) {
        return new FindClienteByIdUsecase(clienteRepository);
    }
}
