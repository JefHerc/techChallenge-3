package com.fiap.gestao_servicos.infrastructure.mapper.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.infrastructure.controller.cliente.ClienteResponseDto;

public class ClienteDomainToResponseMapper {

    public static ClienteResponseDto toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteResponseDto response = new ClienteResponseDto();
        response.setId(cliente.getId());
        response.setNome(cliente.getNome());
        response.setCpf(cliente.getCpf().getValue());
        response.setCelular(cliente.getCelular().getValue());
        response.setEmail(cliente.getEmail().getValue());
        response.setSexo(cliente.getSexo().name());

        return response;
    }
}