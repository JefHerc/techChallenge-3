package com.fiap.gestao_servicos.infrastructure.persistence.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepositoryJpa extends JpaRepository<AgendamentoEntity, Long> {
}