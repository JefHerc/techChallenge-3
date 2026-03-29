package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstabelecimentoRepositoryJpa extends JpaRepository<EstabelecimentoEntity, Long>,
        JpaSpecificationExecutor<EstabelecimentoEntity> {
	boolean existsByCnpj(String cnpj);
	boolean existsByCnpjAndNome(String cnpj, String nome);
	boolean existsByCnpjAndIdNot(String cnpj, Long id);

	@Query("SELECT AVG(av.notaEstabelecimento) FROM AvaliacaoEntity av " +
	       "WHERE av.agendamento.estabelecimento.id = :estabelecimentoId " +
	       "AND av.agendamento.status = 'CONCLUIDO'")
	Double findAverageNota(@Param("estabelecimentoId") Long estabelecimentoId);

	@Query("SELECT AVG(av.notaProfissional) FROM AvaliacaoEntity av " +
	       "WHERE av.agendamento.profissional.id = :profissionalId " +
	       "AND av.agendamento.status = 'CONCLUIDO'")
	Double findAverageProfissionalNota(@Param("profissionalId") Long profissionalId);

	@Query("SELECT sp FROM ServicoProfissionalEntity sp " +
	       "JOIN sp.profissional p " +
	       "JOIN sp.servico s " +
	       "WHERE p.estabelecimento.id = :estabelecimentoId " +
	       "AND LOWER(s.nome) LIKE LOWER(CONCAT('%', :servicoNome, '%'))")
	List<ServicoProfissionalEntity> findServicosOfertadosByEstabelecimentoAndServico(
	    @Param("estabelecimentoId") Long estabelecimentoId,
	    @Param("servicoNome") String servicoNome
	);
}


