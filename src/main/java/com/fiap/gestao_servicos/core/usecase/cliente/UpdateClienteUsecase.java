package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;

public class UpdateClienteUsecase {

    private final ClienteRepository clienteRepository;

    public UpdateClienteUsecase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente update(Long id, Cliente cliente) {
        Cliente clienteAtual = clienteRepository.findById(id);
        if (clienteAtual == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para o id: " + id);
        }

        validarDuplicidade(clienteAtual, cliente);

        Cliente atualizado = clienteRepository.update(id, cliente);
        if (atualizado == null) {
            throw new ResourceNotFoundException("Cliente não encontrado para o id: " + id);
        }

        return atualizado;
    }

    private void validarDuplicidade(Cliente clienteAtual, Cliente novoCliente) {
        String cpfAtual = clienteAtual.getCpf().getValue();
        String cpfNovo = novoCliente.getCpf().getValue();
        if (!cpfAtual.equals(cpfNovo) && clienteRepository.existsByCpf(cpfNovo)) {
            throw new DuplicateDataException("Já existe um cliente com o mesmo CPF.");
        }

        String celularAtual = clienteAtual.getCelular().getValue();
        String celularNovo = novoCliente.getCelular().getValue();
        if (!celularAtual.equals(celularNovo) && clienteRepository.existsByCelular(celularNovo)) {
            throw new DuplicateDataException("Já existe um cliente com o mesmo celular.");
        }

        String emailAtual = clienteAtual.getEmail().getValue();
        String emailNovo = novoCliente.getEmail().getValue();
        if (!emailAtual.equalsIgnoreCase(emailNovo) && clienteRepository.existsByEmail(emailNovo)) {
            throw new DuplicateDataException("Já existe um cliente com o mesmo email.");
        }
    }
}