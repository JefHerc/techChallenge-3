package com.fiap.gestao_servicos.infrastructure.mapper.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoResponseDto;

public class ServicoDomainToResponseMapper {

    public static ServicoResponseDto toResponse(Servico servico) {
        if (servico == null) {
            return null;
        }

        ServicoResponseDto response = new ServicoResponseDto();
        response.setId(servico.getId());
        response.setNome(servico.getNome());
        response.setDuracaoMedia(servico.getDuracaoMedia());
        return response;
    }
}