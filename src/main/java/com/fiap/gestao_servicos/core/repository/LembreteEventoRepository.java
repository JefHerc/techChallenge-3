package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;

import java.util.List;

public interface LembreteEventoRepository {

    LembreteEvento save(LembreteEvento evento);

    List<LembreteEvento> findTop200Pendentes();

    boolean existsByAgendamentoIdAndTipoAndDestinatario(Long agendamentoId,
                                                        LembreteTipo tipo,
                                                        LembreteDestinatario destinatario);
}
