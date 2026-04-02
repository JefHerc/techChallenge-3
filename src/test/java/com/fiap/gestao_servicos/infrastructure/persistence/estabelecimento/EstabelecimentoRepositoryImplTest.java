package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoSearchResult;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EstabelecimentoRepositoryImplTest {

    @Test
    void deveCriarEstabelecimento() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        Estabelecimento input = estabelecimentoDomain();
        EstabelecimentoEntity saved = estabelecimentoEntity(1L);

        when(jpaRepository.save(any(EstabelecimentoEntity.class))).thenReturn(saved);

        Estabelecimento result = repository.create(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Studio Teste", result.getNome());
    }

    @Test
    void deveNormalizarCnpjNosMetodosDeExistenciaETemProfissional() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        when(jpaRepository.existsByCnpj("64245654000168")).thenReturn(true);
        when(jpaRepository.existsByCnpjAndNome("64245654000168", "Studio Teste")).thenReturn(true);
        when(jpaRepository.existsByCnpjAndIdNot("64245654000168", 5L)).thenReturn(false);
        when(profissionalRepositoryJpa.existsByIdAndEstabelecimento_Id(7L, 5L)).thenReturn(true);

        assertTrue(repository.existsByCnpj("64.245.654/0001-68"));
        assertTrue(repository.existsByCnpjAndNome("64.245.654/0001-68", "Studio Teste"));
        assertEquals(false, repository.existsByCnpjAndIdNot("64.245.654/0001-68", 5L));
        assertTrue(repository.temProfissional(5L, 7L));
    }

    @Test
    void deveRetornarNuloQuandoUpdateNaoEncontrarRegistro() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Estabelecimento result = repository.update(99L, estabelecimentoDomain());

        assertEquals(null, result);
    }

    @Test
    void deveLancarDuplicateDataExceptionNoUpdateQuandoIntegridadeViolar() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(estabelecimentoEntity(1L)));
        when(jpaRepository.save(any(EstabelecimentoEntity.class))).thenThrow(new DataIntegrityViolationException("dup"));

        assertThrows(DuplicateDataException.class, () -> repository.update(1L, estabelecimentoDomain()));
    }

    @Test
    void deveLancarDuplicateDataExceptionNoUpdateDadosCadastraisQuandoIntegridadeViolar() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(estabelecimentoEntity(1L)));
        when(jpaRepository.save(any(EstabelecimentoEntity.class))).thenThrow(new DataIntegrityViolationException("dup"));

        assertThrows(DuplicateDataException.class, () -> repository.updateDadosCadastrais(1L, estabelecimentoDomain()));
    }

    @Test
    void deveBuscarPorIdFindAllDeleteAndExists() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        EstabelecimentoEntity entity = estabelecimentoEntity(10L);
        PageImpl<EstabelecimentoEntity> page = new PageImpl<>(List.of(entity));

        when(jpaRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(jpaRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(jpaRepository.existsById(10L)).thenReturn(true);

        Optional<Estabelecimento> byId = repository.findById(10L);
        PageResult<Estabelecimento> all = repository.findAll(new PageQuery(0, 10, List.of()));
        repository.deleteById(10L);

        assertTrue(byId.isPresent());
        assertEquals(1, all.getContent().size());
        assertTrue(repository.existsById(10L));
        verify(jpaRepository).deleteById(10L);
    }

    @Test
    void deveBuscarPorCriteriaPreenchendoNota() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        EstabelecimentoEntity entity = estabelecimentoEntity(11L);
        PageImpl<EstabelecimentoEntity> page = new PageImpl<>(List.of(entity));

        when(jpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(jpaRepository.findAverageNota(11L)).thenReturn(4.5);

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        PageResult<Estabelecimento> result = repository.findByCriteria(filter, new PageQuery(0, 10, List.of()));

        assertEquals(1, result.getContent().size());
        assertEquals(4.5, result.getContent().get(0).getNota());
    }

    @Test
    void deveBuscarPorCriteriaComServicosQuandoFiltroServicoForInformado() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        EstabelecimentoEntity entity = estabelecimentoEntity(12L);
        PageImpl<EstabelecimentoEntity> page = new PageImpl<>(List.of(entity));

        ServicoEntity servico = new ServicoEntity("CORTE", Duration.ofMinutes(30));
        servico.setId(100L);
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setId(200L);
        profissional.setNome("Profissional 1");
        profissional.setEmail("profissional@teste.com");
        profissional.setCelular("11999998888");
        profissional.setUrlFoto("https://foto");

        ServicoProfissionalEntity sp = new ServicoProfissionalEntity();
        sp.setServico(servico);
        sp.setProfissional(profissional);
        sp.setValor(new BigDecimal("89.90"));

        when(jpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(jpaRepository.findAverageNota(12L)).thenReturn(4.2);
        when(jpaRepository.findServicosOfertadosByEstabelecimentoAndServico(12L, "CORTE")).thenReturn(List.of(sp));
        when(jpaRepository.findAverageProfissionalNota(200L)).thenReturn(4.7);

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        filter.setServicoNome("CORTE");

        PageResult<EstabelecimentoSearchResult> result = repository.findByCriteriaWithServices(
                filter,
                new PageQuery(0, 10, List.of())
        );

        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getServicosOfertados().size());
        assertEquals(100L, result.getContent().get(0).getServicosOfertados().get(0).getServicoId());
        verify(jpaRepository).findServicosOfertadosByEstabelecimentoAndServico(12L, "CORTE");
    }

    @Test
    void deveBuscarPorCriteriaComServicosVaziosQuandoFiltroServicoNaoInformado() {
        EstabelecimentoRepositoryJpa jpaRepository = mock(EstabelecimentoRepositoryJpa.class);
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        EstabelecimentoRepositoryImpl repository = new EstabelecimentoRepositoryImpl(jpaRepository, profissionalRepositoryJpa);

        EstabelecimentoEntity entity = estabelecimentoEntity(13L);
        PageImpl<EstabelecimentoEntity> page = new PageImpl<>(List.of(entity));

        when(jpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(jpaRepository.findAverageNota(13L)).thenReturn(3.9);

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        PageResult<EstabelecimentoSearchResult> result = repository.findByCriteriaWithServices(
                filter,
                new PageQuery(0, 10, List.of())
        );

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getServicosOfertados().isEmpty());
    }

    private Estabelecimento estabelecimentoDomain() {
        return new Estabelecimento(
                null,
                "Studio Teste",
                new Endereco("Rua Teste", "123", null, "Centro", "Sao Paulo", "SP", "01001000"),
                null,
                null,
                new Cnpj("64245654000168"),
                List.of("https://foto"),
                List.of(),
                null
        );
    }

    private EstabelecimentoEntity estabelecimentoEntity(Long id) {
        EstabelecimentoEntity entity = new EstabelecimentoEntity();
        entity.setId(id);
        entity.setNome("Studio Teste");
        entity.setCnpj("64245654000168");
        entity.setEndereco(new com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity(
                "Rua Teste", "123", null, "Centro", "Sao Paulo", "SP", "01001000"
        ));
        entity.setUrlFotos(List.of("https://foto"));
        entity.setHorarioFuncionamento(List.of());
        return entity;
    }
}
