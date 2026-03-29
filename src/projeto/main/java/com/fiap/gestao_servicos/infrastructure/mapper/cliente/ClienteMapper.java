package com.fiap.gestao_servicos.infrastructure.mapper.cliente;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.infrastructure.controller.cliente.ClienteDto;
import com.fiap.gestao_servicos.infrastructure.controller.cliente.ClienteResponseDto;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toDomain(ClienteDto dto) {
        if (dto == null) {
            return null;
        }

        return new Cliente(
                dto.getNome(),
                new Cpf(dto.getCpf()),
                new Celular(dto.getCelular()),
                new Email(dto.getEmail()),
                parseSexo(dto.getSexo())
        );
    }

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

    private static Sexo parseSexo(String sexo) {
        if (sexo == null || sexo.trim().isEmpty()) {
            throw new IllegalArgumentException("Sexo não pode ser nulo ou vazio");
        }

        try {
            return Sexo.valueOf(sexo.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Sexo inválido. Valores permitidos: MASCULINO, FEMININO, OUTRO");
        }
    }
}