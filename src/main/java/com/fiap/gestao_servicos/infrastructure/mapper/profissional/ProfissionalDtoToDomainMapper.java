package com.fiap.gestao_servicos.infrastructure.mapper.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalDto;

public class ProfissionalDtoToDomainMapper {

    public static Profissional toDomain(ProfissionalDto dto) {
        if (dto == null) {
            return null;
        }

        return new Profissional(
                dto.getNome(),
                new Cpf(dto.getCpf()),
                new Celular(dto.getCelular()),
                new Email(dto.getEmail()),
                dto.getUrlFoto(),
                dto.getDescricao()
        );
    }
}