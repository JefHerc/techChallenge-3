package com.fiap.gestao_servicos.infrastructure.persistence.notificacao;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LembreteEventoRepositoryJpa extends JpaRepository<LembreteEventoEntity, Long> {

    boolean existsByAgendamento_IdAndTipoAndDestinatario(Long agendamentoId,
                                                         LembreteTipo tipo,
                                                         LembreteDestinatario destinatario);

    List<LembreteEventoEntity> findTop200ByStatusOrderByCriadoEmAsc(LembreteStatus status);
}


