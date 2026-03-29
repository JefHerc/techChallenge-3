package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepositoryJpa extends JpaRepository<ServicoEntity, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndEstabelecimento_Id(String nome, Long estabelecimentoId);

    Page<ServicoEntity> findAllByEstabelecimento_Id(Long estabelecimentoId, Pageable pageable);

    java.util.List<ServicoEntity> findAllByEstabelecimento_Id(Long estabelecimentoId);

    java.util.Optional<ServicoEntity> findByIdAndEstabelecimento_Id(Long id, Long estabelecimentoId);

    boolean existsByIdAndEstabelecimento_Id(Long id, Long estabelecimentoId);
}

