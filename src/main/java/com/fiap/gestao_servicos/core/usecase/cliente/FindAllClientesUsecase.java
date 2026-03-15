package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindAllClientesUsecase {

    private final ClienteRepository clienteRepository;

    public FindAllClientesUsecase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
}