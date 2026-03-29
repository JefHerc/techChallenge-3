package com.fiap.gestao_servicos.infrastructure.persistence.avaliacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepositoryJpa extends JpaRepository<AvaliacaoEntity, Long> {
	Page<AvaliacaoEntity> findByAgendamentoId(Long agendamentoId, Pageable pageable);
}

