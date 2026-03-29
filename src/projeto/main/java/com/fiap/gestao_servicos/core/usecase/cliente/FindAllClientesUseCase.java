package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllClientesUseCase {

    private final ClienteRepository clienteRepository;

    public FindAllClientesUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }
}

