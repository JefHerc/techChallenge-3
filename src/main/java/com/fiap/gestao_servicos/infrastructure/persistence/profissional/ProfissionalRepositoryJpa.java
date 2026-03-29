package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepositoryJpa extends JpaRepository<ProfissionalEntity, Long> {

	boolean existsByCpf(String cpf);
	boolean existsByCelular(String celular);
	boolean existsByEmail(String email);
	Page<ProfissionalEntity> findAllByEstabelecimento_Id(Long estabelecimentoId, Pageable pageable);
	java.util.List<ProfissionalEntity> findAllByEstabelecimento_Id(Long estabelecimentoId);
	java.util.Optional<ProfissionalEntity> findByIdAndEstabelecimento_Id(Long id, Long estabelecimentoId);
	boolean existsByIdAndEstabelecimento_Id(Long id, Long estabelecimentoId);
	boolean existsByIdAndServicosProfissionalServicoId(Long profissionalId, Long servicoId);
}

