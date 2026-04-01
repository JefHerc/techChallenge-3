package com.fiap.gestao_servicos.infrastructure.persistence.notificacao;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.core.repository.LembreteEventoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.notificacao.LembreteEventoMapper;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

@Component
public class LembreteEventoRepositoryImpl implements LembreteEventoRepository {

    private final LembreteEventoRepositoryJpa lembreteEventoRepositoryJpa;

    public LembreteEventoRepositoryImpl(LembreteEventoRepositoryJpa lembreteEventoRepositoryJpa) {
        this.lembreteEventoRepositoryJpa = lembreteEventoRepositoryJpa;
    }

    @Override
    public LembreteEvento save(LembreteEvento evento) {
        LembreteEventoEntity entity = LembreteEventoMapper.toEntity(evento);
        LembreteEventoEntity saved = lembreteEventoRepositoryJpa.save(entity);
        return evento.comId(saved.getId());
    }

    @Override
    public List<LembreteEvento> findTop200Pendentes() {
        return lembreteEventoRepositoryJpa
                .findTop200ByStatusInOrderByCriadoEmAsc(EnumSet.of(LembreteStatus.PENDENTE, LembreteStatus.FALHA))
                .stream()
                .map(LembreteEventoMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByAgendamentoIdAndTipoAndDestinatario(Long agendamentoId,
                                                               LembreteTipo tipo,
                                                               LembreteDestinatario destinatario) {
        return lembreteEventoRepositoryJpa
                .existsByAgendamento_IdAndTipoAndDestinatario(agendamentoId, tipo, destinatario);
    }
}
