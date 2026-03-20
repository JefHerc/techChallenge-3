package com.fiap.gestao_servicos.infrastructure.mapper.cliente;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.infrastructure.controller.cliente.ClienteDto;

public class ClienteDtoToDomainMapper {

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