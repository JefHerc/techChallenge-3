package com.fiap.gestao_servicos.infrastructure.mapper.avaliacao;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.infrastructure.controller.avaliacao.AvaliacaoDto;
import com.fiap.gestao_servicos.infrastructure.controller.avaliacao.AvaliacaoResponseDto;

public final class AvaliacaoMapper {

    private AvaliacaoMapper() {
    }

    public static Avaliacao toDomain(AvaliacaoDto dto) {
        throw new UnsupportedOperationException("Use toDomain(AvaliacaoDto, Agendamento) para mapear avaliacao");
    }

    public static Avaliacao toDomain(AvaliacaoDto dto, Agendamento agendamento) {
        if (dto == null) {
            return null;
        }
        if (agendamento == null) {
            throw new IllegalArgumentException("agendamento nao pode ser nulo");
        }

        return new Avaliacao(
                null,
                agendamento,
                dto.getNotaEstabelecimento(),
                dto.getNotaProfissional(),
                dto.getComentarioEstabelecimento() != null ? dto.getComentarioEstabelecimento() : "",
                dto.getComentarioProfissional() != null ? dto.getComentarioProfissional() : ""
        );
    }

    public static AvaliacaoResponseDto toResponse(Avaliacao avaliacao) {
        if (avaliacao == null) {
            return null;
        }

        AvaliacaoResponseDto response = new AvaliacaoResponseDto();
        response.setId(avaliacao.getId());
        response.setAgendamentoId(avaliacao.getAgendamento().getId());
        response.setProfissionalId(avaliacao.getAgendamento().getProfissional().getId());
        response.setProfissionalNome(avaliacao.getAgendamento().getProfissional().getNome());
        response.setEstabelecimentoId(avaliacao.getAgendamento().getEstabelecimento().getId());
        response.setEstabelecimentoNome(avaliacao.getAgendamento().getEstabelecimento().getNome());
        response.setStatusAgendamento(avaliacao.getAgendamento().getStatus().name());
        response.setNotaEstabelecimento(avaliacao.getNotaEstabelecimento());
        response.setNotaProfissional(avaliacao.getNotaProfissional());
        response.setComentarioEstabelecimento(avaliacao.getComentarioEstabelecimento());
        response.setComentarioProfissional(avaliacao.getComentarioProfissional());
        return response;
    }

}