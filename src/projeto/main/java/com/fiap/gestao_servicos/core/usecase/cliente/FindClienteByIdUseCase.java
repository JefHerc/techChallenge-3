package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;

public class FindClienteByIdUseCase {

    private final ClienteRepository clienteRepository;

    public FindClienteByIdUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Cliente",
                        id)));
    }
}

