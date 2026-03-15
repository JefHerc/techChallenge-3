package com.fiap.gestao_servicos.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstabelecimentoRepositoryJpa extends JpaRepository<EstabelecimentoEntity, Long> {
	boolean existsByCnpjAndNome(String cnpj, String nome);
}
