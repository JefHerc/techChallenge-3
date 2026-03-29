package com.fiap.gestao_servicos.infrastructure.mapper.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoDto;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoResponseDto;

public final class ServicoMapper {

    private ServicoMapper() {
    }

    public static Servico toDomain(ServicoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Servico(parseNome(dto.getNome()), dto.getDuracaoMedia());
    }

    public static ServicoResponseDto toResponse(Servico servico) {
        if (servico == null) {
            return null;
        }

        ServicoResponseDto response = new ServicoResponseDto();
        response.setId(servico.getId());
        response.setNome(servico.getNomeDescricao());
        response.setDuracaoMedia(servico.getDuracaoMedia());
        return response;
    }

    public static Servico toReference(Long servicoId) {
        if (servicoId == null) {
            return null;
        }
        return new Servico(servicoId, ServicoEnum.HIDRATACAO, java.time.Duration.ofMinutes(1));
    }

    private static ServicoEnum parseNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do serviço não pode ser nulo ou vazio");
        }

        for (ServicoEnum servicoEnum : ServicoEnum.values()) {
            if (servicoEnum.name().equalsIgnoreCase(nome.trim())
                    || servicoEnum.getDescricao().equalsIgnoreCase(nome.trim())) {
                return servicoEnum;
            }
        }

        throw new IllegalArgumentException("Serviço inválido: " + nome);
    }
}