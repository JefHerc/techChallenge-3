package com.fiap.gestao_servicos.infrastructure.mapper.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.infrastructure.controller.agendamento.AgendamentoResponseDto;

public class AgendamentoDomainToResponseMapper {

    public static AgendamentoResponseDto toResponse(Agendamento agendamento) {
        if (agendamento == null) {
            return null;
        }

        AgendamentoResponseDto response = new AgendamentoResponseDto();
        response.setId(agendamento.getId());
        response.setProfissionalId(agendamento.getProfissional().getId());
        response.setProfissionalNome(agendamento.getProfissional().getNome());
        response.setServicoId(agendamento.getServico().getId());
        response.setServicoNome(agendamento.getServico().getNome());
        response.setEstabelecimentoId(agendamento.getEstabelecimento().getId());
        response.setEstabelecimentoNome(agendamento.getEstabelecimento().getNome());
        response.setClienteId(agendamento.getCliente().getId());
        response.setClienteNome(agendamento.getCliente().getNome());
        response.setDataHoraInicio(agendamento.getDataHoraInicio());
        response.setDataHoraFim(agendamento.getDataHoraFim());
        response.setStatus(agendamento.getStatus().name());

        return response;
    }
}