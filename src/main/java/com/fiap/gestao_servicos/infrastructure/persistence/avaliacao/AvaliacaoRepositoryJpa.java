package com.fiap.gestao_servicos.infrastructure.persistence.avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepositoryJpa extends JpaRepository<AvaliacaoEntity, Long> {

    @Query("""
            select (count(a) > 0)
            from AgendamentoEntity a
            where a.id = :agendamentoId
              and a.profissional.id = :profissionalId
              and a.estabelecimento.id = :estabelecimentoId
            """)
    boolean existsAgendamentoByIdAndProfissionalAndEstabelecimento(@Param("agendamentoId") Long agendamentoId,
                                                                    @Param("profissionalId") Long profissionalId,
                                                                    @Param("estabelecimentoId") Long estabelecimentoId);
}