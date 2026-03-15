package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DeleteClienteUsecase {

    private final ClienteRepository clienteRepository;

    public DeleteClienteUsecase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void deleteById(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado para o id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}