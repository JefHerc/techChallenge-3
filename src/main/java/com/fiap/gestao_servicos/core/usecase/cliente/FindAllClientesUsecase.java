package com.fiap.gestao_servicos.core.usecase.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;

public class FindAllClientesUseCase {

    private final ClienteRepository clienteRepository;

    public FindAllClientesUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public PageResult<Cliente> findAll(PageQuery pageQuery) {
        return clienteRepository.findAll(pageQuery);
    }
}

