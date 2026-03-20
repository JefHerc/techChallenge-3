package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepositoryJpa extends JpaRepository<ProfissionalEntity, Long> {

	boolean existsByCpf(String cpf);
	boolean existsByCelular(String celular);
	boolean existsByEmail(String email);
}