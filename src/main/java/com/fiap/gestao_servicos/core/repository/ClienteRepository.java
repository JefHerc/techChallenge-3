package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;

import java.util.Optional;

public interface ClienteRepository {

    Cliente create(Cliente cliente);
    Cliente update(Long id, Cliente cliente);
    void deleteById(Long id);
    PageResult<Cliente> findAll(PageQuery pageQuery);
    Optional<Cliente> findById(Long id);
    boolean existsById(Long id);
    boolean existsByCpf(String cpf);
    boolean existsByCelular(String celular);
    boolean existsByEmail(String email);
}

