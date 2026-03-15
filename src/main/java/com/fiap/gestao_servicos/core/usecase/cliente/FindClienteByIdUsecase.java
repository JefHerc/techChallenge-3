package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FindClienteByIdUsecase {

    private final ClienteRepository clienteRepository;

    public FindClienteByIdUsecase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente findById(Long id) {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para o id: " + id);
        }
        return cliente;
    }
}