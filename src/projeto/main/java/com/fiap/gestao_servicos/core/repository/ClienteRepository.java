package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClienteRepository {

    Cliente create(Cliente cliente);
    Cliente update(Long id, Cliente cliente);
    void deleteById(Long id);
    Page<Cliente> findAll(Pageable pageable);
    Optional<Cliente> findById(Long id);
    boolean existsById(Long id);
    boolean existsByCpf(String cpf);
    boolean existsByCelular(String celular);
    boolean existsByEmail(String email);
}

