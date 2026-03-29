package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EstabelecimentoRepositoryJpa extends JpaRepository<EstabelecimentoEntity, Long>,
        JpaSpecificationExecutor<EstabelecimentoEntity> {
	boolean existsByCnpj(String cnpj);
	boolean existsByCnpjAndNome(String cnpj, String nome);
	boolean existsByCnpjAndIdNot(String cnpj, Long id);
	boolean existsByIdAndProfissionais_Id(Long estabelecimentoId, Long profissionalId);
}


