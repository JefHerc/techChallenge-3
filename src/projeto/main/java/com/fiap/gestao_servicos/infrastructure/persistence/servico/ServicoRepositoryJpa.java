package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepositoryJpa extends JpaRepository<ServicoEntity, Long> {

	boolean existsByNomeIgnoreCase(String nome);
}

