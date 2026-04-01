package com.fiap.gestao_servicos.infrastructure.persistence.agendamento;

import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepositoryJpa extends JpaRepository<AgendamentoEntity, Long> {
    boolean existsByIdAndStatus(Long id, AgendamentoStatus status);

    boolean existsByIdAndProfissionalIdAndEstabelecimentoId(
            Long id,
            Long profissionalId,
            Long estabelecimentoId);

    List<AgendamentoEntity> findByStatusAndDataHoraInicioBetween(
            AgendamentoStatus status,
            LocalDateTime inicio,
            LocalDateTime fim);

    Page<AgendamentoEntity> findByEstabelecimentoId(
            Long estabelecimentoId,
            Pageable pageable);

    List<AgendamentoEntity> findByEstabelecimentoIdAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
            Long estabelecimentoId,
            LocalDateTime inicio,
            LocalDateTime fim);

    @Query("""
                                                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                                                FROM AgendamentoEntity a
                                                WHERE a.profissional.id = :profissionalId
                                                        AND a.status = :status
                                                        AND (:agendamentoIdIgnorar IS NULL OR a.id <> :agendamentoIdIgnorar)
                                                        AND a.dataHoraInicio < :novoFim
                                                        AND a.dataHoraFim > :novoInicio
                                                """)
    boolean existsConflitoHorarioProfissional(
            @Param("profissionalId") Long profissionalId,
            @Param("novoInicio") LocalDateTime novoInicio,
            @Param("novoFim") LocalDateTime novoFim,
            @Param("status") AgendamentoStatus status,
            @Param("agendamentoIdIgnorar") Long agendamentoIdIgnorar);
}

