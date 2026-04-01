package com.fiap.gestao_servicos.infrastructure.mapper.notificacao;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.notificacao.LembreteEventoEntity;

public final class LembreteEventoMapper {

    private LembreteEventoMapper() {
    }

    public static LembreteEvento toDomain(LembreteEventoEntity entity) {
        String destinoEmail = null;
        if (entity.getAgendamento() != null) {
            if (entity.getDestinatario() == LembreteDestinatario.CLIENTE
                    && entity.getAgendamento().getCliente() != null) {
                destinoEmail = entity.getAgendamento().getCliente().getEmail();
            } else if (entity.getDestinatario() == LembreteDestinatario.PROFISSIONAL
                    && entity.getAgendamento().getProfissional() != null) {
                destinoEmail = entity.getAgendamento().getProfissional().getEmail();
            }
        }

        return new LembreteEvento(
                entity.getId(),
                entity.getAgendamento() != null ? entity.getAgendamento().getId() : null,
                entity.getTipo(),
                entity.getDestinatario(),
                entity.getStatus(),
                entity.getMensagem(),
                destinoEmail,
                entity.getCriadoEm(),
                entity.getEnviadoEm(),
                entity.getErro()
        );
    }

    public static LembreteEventoEntity toEntity(LembreteEvento domain) {
        LembreteEventoEntity entity = new LembreteEventoEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(domain.getAgendamentoId());
        entity.setAgendamento(agendamento);
        entity.setTipo(domain.getTipo());
        entity.setDestinatario(domain.getDestinatario());
        entity.setStatus(domain.getStatus());
        entity.setMensagem(domain.getMensagem());
        entity.setCriadoEm(domain.getCriadoEm());
        entity.setEnviadoEm(domain.getEnviadoEm());
        entity.setErro(domain.getErro());
        return entity;
    }
}
