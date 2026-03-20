package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;

public class CreateClienteUsecase {

    private final ClienteRepository clienteRepository;

    public CreateClienteUsecase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente create(Cliente cliente) {
        validarDuplicidade(cliente);
        return clienteRepository.create(cliente);
    }

    private void validarDuplicidade(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf().getValue())) {
            throw new DuplicateDataException("Já existe um cliente com o mesmo CPF.");
        }
        if (clienteRepository.existsByCelular(cliente.getCelular().getValue())) {
            throw new DuplicateDataException("Já existe um cliente com o mesmo celular.");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail().getValue())) {
            throw new DuplicateDataException("Já existe um cliente com o mesmo email.");
        }
    }
}