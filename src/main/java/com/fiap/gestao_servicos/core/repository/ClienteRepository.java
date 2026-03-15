package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Cliente;

import java.util.List;

public interface ClienteRepository {

    Cliente create(Cliente cliente);
    Cliente update(Long id, Cliente cliente);
    void deleteById(Long id);
    List<Cliente> findAll();
    Cliente findById(Long id);
    boolean existsById(Long id);
    boolean existsByCpf(String cpf);
    boolean existsByCelular(String celular);
    boolean existsByEmail(String email);
}