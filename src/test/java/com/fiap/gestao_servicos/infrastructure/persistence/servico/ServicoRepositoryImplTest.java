package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServicoRepositoryImplTest {

    @Test
    void deveCriarServicoSemEstabelecimento() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity savedEntity = new ServicoEntity();
        savedEntity.setId(1L);
        savedEntity.setNome("CORTE");
        savedEntity.setDuracaoMedia(Duration.ofMinutes(30));

        when(jpaRepository.save(any(ServicoEntity.class))).thenReturn(savedEntity);

        Servico servico = new Servico(null, ServicoEnum.CORTE, Duration.ofMinutes(30));
        Servico resultado = repository.create(servico);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(ServicoEnum.CORTE, resultado.getNome());
        assertEquals(Duration.ofMinutes(30), resultado.getDuracaoMedia());
        verify(jpaRepository, times(1)).save(any(ServicoEntity.class));
    }

    @Test
    void deveCriarServicoComEstabelecimento() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity savedEntity = new ServicoEntity();
        savedEntity.setId(2L);
        savedEntity.setNome("MASSAGEM");
        savedEntity.setDuracaoMedia(Duration.ofMinutes(60));
        EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
        estabelecimento.setId(10L);
        savedEntity.setEstabelecimento(estabelecimento);

        when(jpaRepository.save(any(ServicoEntity.class))).thenReturn(savedEntity);

        Servico servico = new Servico(null, ServicoEnum.MASSAGEM, Duration.ofMinutes(60));
        Servico resultado = repository.create(10L, servico);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals(ServicoEnum.MASSAGEM, resultado.getNome());
        assertEquals(Duration.ofMinutes(60), resultado.getDuracaoMedia());
    }

    @Test
    void deveAtualizarServico() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity existingEntity = new ServicoEntity();
        existingEntity.setId(3L);
        existingEntity.setNome("CORTE");
        existingEntity.setDuracaoMedia(Duration.ofMinutes(30));

        ServicoEntity updatedEntity = new ServicoEntity();
        updatedEntity.setId(3L);
        updatedEntity.setNome("MANICURE");
        updatedEntity.setDuracaoMedia(Duration.ofMinutes(45));

        when(jpaRepository.findById(3L)).thenReturn(Optional.of(existingEntity));
        when(jpaRepository.save(any(ServicoEntity.class))).thenReturn(updatedEntity);

        Servico servico = new Servico(3L, ServicoEnum.MANICURE, Duration.ofMinutes(45));
        Servico resultado = repository.update(3L, servico);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals(ServicoEnum.MANICURE, resultado.getNome());
        assertEquals(Duration.ofMinutes(45), resultado.getDuracaoMedia());
    }

    @Test
    void deveRetornarNuloAoAtualizarServicoNaoExistente() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        Servico servico = new Servico(999L, ServicoEnum.CORTE, Duration.ofMinutes(30));
        Servico resultado = repository.update(999L, servico);

        assertNull(resultado);
    }

    @Test
    void deveDeletarServicoPorId() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        repository.deleteById(5L);

        verify(jpaRepository, times(1)).deleteById(5L);
    }

    @Test
    void deveBuscarTodosServicosPaginado() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity entity1 = new ServicoEntity();
        entity1.setId(1L);
        entity1.setNome("CORTE");
        entity1.setDuracaoMedia(Duration.ofMinutes(30));

        ServicoEntity entity2 = new ServicoEntity();
        entity2.setId(2L);
        entity2.setNome("MASSAGEM");
        entity2.setDuracaoMedia(Duration.ofMinutes(60));

        Page<ServicoEntity> page = new PageImpl<>(List.of(entity1, entity2), PageRequest.of(0, 10), 2);
        when(jpaRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageQuery pageQuery = new PageQuery(1, 10, List.of());
        PageResult<Servico> resultado = repository.findAll(pageQuery);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals(ServicoEnum.CORTE, resultado.getContent().get(0).getNome());
        assertEquals(ServicoEnum.MASSAGEM, resultado.getContent().get(1).getNome());
    }

    @Test
    void deveBuscarServicoPorId() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity entity = new ServicoEntity();
        entity.setId(10L);
        entity.setNome("CORTE");
        entity.setDuracaoMedia(Duration.ofMinutes(30));

        when(jpaRepository.findById(10L)).thenReturn(Optional.of(entity));

        Optional<Servico> resultado = repository.findById(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
        assertEquals(ServicoEnum.CORTE, resultado.get().getNome());
    }

    @Test
    void deveRetornarOptionalVazioQuandoServicoPorIdNaoExiste() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Servico> resultado = repository.findById(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveBuscarServicoPorIdEEstabelecimentoId() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity entity = new ServicoEntity();
        entity.setId(11L);
        entity.setNome("MASSAGEM");
        entity.setDuracaoMedia(Duration.ofMinutes(60));
        EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
        estabelecimento.setId(20L);
        entity.setEstabelecimento(estabelecimento);

        when(jpaRepository.findByIdAndEstabelecimento_Id(11L, 20L)).thenReturn(Optional.of(entity));

        Optional<Servico> resultado = repository.findByIdAndEstabelecimentoId(11L, 20L);

        assertTrue(resultado.isPresent());
        assertEquals(11L, resultado.get().getId());
    }

    @Test
    void deveBuscarTodosServicosPorEstabelecimentoIdPaginado() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity entity = new ServicoEntity();
        entity.setId(12L);
        entity.setNome("CORTE");
        entity.setDuracaoMedia(Duration.ofMinutes(30));

        Page<ServicoEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        when(jpaRepository.findAllByEstabelecimento_Id(eq(30L), any(Pageable.class))).thenReturn(page);

        PageQuery pageQuery = new PageQuery(1, 10, List.of());
        PageResult<Servico> resultado = repository.findPageByEstabelecimentoId(30L, pageQuery);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(12L, resultado.getContent().get(0).getId());
    }

    @Test
    void deveBuscarTodosServicosPorEstabelecimentoIdSemPaginacao() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity entity1 = new ServicoEntity();
        entity1.setId(13L);
        entity1.setNome("CORTE");
        entity1.setDuracaoMedia(Duration.ofMinutes(30));

        ServicoEntity entity2 = new ServicoEntity();
        entity2.setId(14L);
        entity2.setNome("BARBA");
        entity2.setDuracaoMedia(Duration.ofMinutes(20));

        when(jpaRepository.findAllByEstabelecimento_Id(40L)).thenReturn(List.of(entity1, entity2));

        List<Servico> resultado = repository.findAllByEstabelecimentoId(40L);

        assertEquals(2, resultado.size());
        assertEquals(13L, resultado.get(0).getId());
        assertEquals(14L, resultado.get(1).getId());
    }

    @Test
    void deveVerificarExistenciaServicoById() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        when(jpaRepository.existsById(50L)).thenReturn(true);
        when(jpaRepository.existsById(999L)).thenReturn(false);

        assertTrue(repository.existsById(50L));
        assertFalse(repository.existsById(999L));
    }

    @Test
    void deveVerificarExistenciaServicoByIdEEstabelecimentoId() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        when(jpaRepository.existsByIdAndEstabelecimento_Id(60L, 70L)).thenReturn(true);
        when(jpaRepository.existsByIdAndEstabelecimento_Id(61L, 70L)).thenReturn(false);

        assertTrue(repository.existsByIdAndEstabelecimentoId(60L, 70L));
        assertFalse(repository.existsByIdAndEstabelecimentoId(61L, 70L));
    }

    @Test
    void deveVerificarExistenciaServicoByNomeIgnoreCase() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        when(jpaRepository.existsByNomeIgnoreCase("CORTE")).thenReturn(true);
        when(jpaRepository.existsByNomeIgnoreCase("INEXISTENTE")).thenReturn(false);

        assertTrue(repository.existsByNomeIgnoreCase("CORTE"));
        assertFalse(repository.existsByNomeIgnoreCase("INEXISTENTE"));
    }

    @Test
    void deveVerificarExistenciaServicoByNomeEEstabelecimentoIdIgnoreCase() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        when(jpaRepository.existsByNomeIgnoreCaseAndEstabelecimento_Id("MASSAGEM", 80L)).thenReturn(true);
        when(jpaRepository.existsByNomeIgnoreCaseAndEstabelecimento_Id("YOGA", 80L)).thenReturn(false);

        assertTrue(repository.existsByNomeIgnoreCaseAndEstabelecimentoId("MASSAGEM", 80L));
        assertFalse(repository.existsByNomeIgnoreCaseAndEstabelecimentoId("YOGA", 80L));
    }

    @Test
    void deverMapearCorretamenteDominioParaEntity() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity savedEntity = new ServicoEntity();
        savedEntity.setId(90L);
        savedEntity.setNome("MANICURE");
        savedEntity.setDuracaoMedia(Duration.ofMinutes(45));

        when(jpaRepository.save(any(ServicoEntity.class))).thenAnswer(invocation -> {
            ServicoEntity arg = invocation.getArgument(0);
            assertEquals("MANICURE", arg.getNome());
            assertEquals(Duration.ofMinutes(45), arg.getDuracaoMedia());
            return savedEntity;
        });

        Servico dominio = new Servico(null, ServicoEnum.MANICURE, Duration.ofMinutes(45));
        Servico resultado = repository.create(dominio);

        assertEquals(90L, resultado.getId());
        assertEquals(ServicoEnum.MANICURE, resultado.getNome());
    }

    @Test
    void deverMantermapeamentoQuandoNomeNulo() {
        ServicoRepositoryJpa jpaRepository = mock(ServicoRepositoryJpa.class);
        ServicoRepositoryImpl repository = new ServicoRepositoryImpl(jpaRepository);

        ServicoEntity entity = new ServicoEntity();
        entity.setId(90L);
        entity.setNome("BARBA");
        entity.setDuracaoMedia(Duration.ofMinutes(45));

        when(jpaRepository.findById(90L)).thenReturn(Optional.of(entity));

        Optional<Servico> resultado = repository.findById(90L);

        assertTrue(resultado.isPresent());
        assertEquals(ServicoEnum.BARBA, resultado.get().getNome());
        assertEquals(Duration.ofMinutes(45), resultado.get().getDuracaoMedia());
    }
}
