package com.fiap.gestao_servicos.infrastructure.mapper.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoDto;

public class ServicoDtoToDomainMapper {

    public static Servico toDomain(ServicoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Servico(
                dto.getNome(),
                dto.getDuracaoMedia()
        );
    }
}