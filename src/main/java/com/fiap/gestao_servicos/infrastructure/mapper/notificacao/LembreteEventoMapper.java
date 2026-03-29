package com.fiap.gestao_servicos.infrastructure.mapper.notificacao;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.notificacao.LembreteEventoEntity;

public final class LembreteEventoMapper {

    private LembreteEventoMapper() {
    }

    public static LembreteEvento toDomain(LembreteEventoEntity entity) {
        String destinoEmail = entity.getDestinatario() == LembreteDestinatario.CLIENTE
                ? entity.getAgendamento().getCliente().getEmail()
                : entity.getAgendamento().getProfissional().getEmail();

        LembreteEvento domain = new LembreteEvento();
        domain.setId(entity.getId());
        domain.setAgendamentoId(entity.getAgendamento().getId());
        domain.setTipo(entity.getTipo());
        domain.setDestinatario(entity.getDestinatario());
        domain.setStatus(entity.getStatus());
        domain.setMensagem(entity.getMensagem());
        domain.setDestinoEmail(destinoEmail);
        domain.setCriadoEm(entity.getCriadoEm());
        domain.setEnviadoEm(entity.getEnviadoEm());
        domain.setErro(entity.getErro());
        return domain;
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
