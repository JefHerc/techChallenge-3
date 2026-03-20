package com.fiap.gestao_servicos.infrastructure.mapper.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalResponseDto;

public class ProfissionalDomainToResponseMapper {

    public static ProfissionalResponseDto toResponse(Profissional profissional) {
        if (profissional == null) {
            return null;
        }

        ProfissionalResponseDto response = new ProfissionalResponseDto();
        response.setId(profissional.getId());
        response.setNome(profissional.getNome());
        response.setCpf(profissional.getCpf().getValue());
        response.setCelular(profissional.getCelular().getValue());
        response.setEmail(profissional.getEmail().getValue());
        response.setUrlFoto(profissional.getUrlFoto());
        response.setDescricao(profissional.getDescricao());
        return response;
    }
}