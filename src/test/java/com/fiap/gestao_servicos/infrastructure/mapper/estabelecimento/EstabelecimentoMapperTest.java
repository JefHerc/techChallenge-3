package com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoSearchResult;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.ProfissionalServicoInfo;
import com.fiap.gestao_servicos.core.domain.ServicoOfertadoSearch;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoSearchResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.HorarioFuncionamentoDto;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.HorarioFuncionamentoEmbeddable;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EstabelecimentoMapperTest {

    @Test
    void deveMapearDtoParaDominioEMapearDominioParaResponse() {
        EnderecoDto enderecoDto = new EnderecoDto("Rua A", "100", "Sala 2", "Centro", "Sao Paulo", "SP", "01001000");
        HorarioFuncionamentoDto horarioAberto = new HorarioFuncionamentoDto("segunda-feira", "09:00", "18:00", false);
        HorarioFuncionamentoDto horarioFechado = new HorarioFuncionamentoDto("domingo", null, null, true);

        EstabelecimentoDto dto = new EstabelecimentoDto(
                "Studio A",
                enderecoDto,
                "64245654000168",
                List.of("https://cdn/1.jpg"),
                List.of(horarioAberto, horarioFechado)
        );

        Estabelecimento domain = EstabelecimentoMapper.toDomain(dto);
        EstabelecimentoResponseDto response = EstabelecimentoMapper.toResponse(domain);

        assertNotNull(domain);
        assertEquals("Studio A", domain.getNome());
        assertEquals("64245654000168", domain.getCnpj().getValue());
        assertEquals(2, domain.getHorarioFuncionamento().size());
        assertEquals(DayOfWeek.SUNDAY, domain.getHorarioFuncionamento().get(1).getDiaSemana());
        assertTrue(domain.getHorarioFuncionamento().get(1).isFechado());

        assertNotNull(response);
        assertEquals("Studio A", response.getNome());
        assertEquals("64245654000168", response.getCnpj());
        assertEquals(2, response.getHorarioFuncionamento().size());
    }

    @Test
    void deveMapearSearchResultComServicosEProfissional() {
        Estabelecimento estabelecimento = new Estabelecimento(
                8L,
                "Studio Busca",
                new Endereco("Rua B", "200", null, "Centro", "Sao Paulo", "SP", "01002000"),
                null,
                null,
                new Cnpj("64245654000168"),
                List.of("https://cdn/x.jpg"),
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), false)),
                4.7
        );

        ProfissionalServicoInfo profissional = new ProfissionalServicoInfo(
                12L,
                "Carla",
                "carla@teste.com",
                "11988887777",
                "https://cdn/carla.jpg",
                4.8
        );

        ServicoOfertadoSearch servico = new ServicoOfertadoSearch(99L, "Corte", new BigDecimal("79.90"), profissional);
        EstabelecimentoSearchResult searchResult = new EstabelecimentoSearchResult(estabelecimento, List.of(servico));

        EstabelecimentoSearchResponseDto response = EstabelecimentoMapper.toResponseForSearch(searchResult);

        assertNotNull(response);
        assertEquals(8L, response.getId());
        assertEquals(1, response.getServicosOfertados().size());
        assertEquals("Corte", response.getServicosOfertados().get(0).getServicoNome());
        assertEquals("Carla", response.getServicosOfertados().get(0).getProfissional().getNome());

        assertNull(EstabelecimentoMapper.toResponseForSearch((EstabelecimentoSearchResult) null));
    }

    @Test
    void deveMapearSearchComEntidadesEConsultarNotaProfissional() {
        EstabelecimentoRepositoryJpa repositoryJpa = mock(EstabelecimentoRepositoryJpa.class);
        when(repositoryJpa.findAverageProfissionalNota(21L)).thenReturn(4.3);

        Estabelecimento estabelecimento = new Estabelecimento(
                9L,
                "Studio Entidade",
                new Endereco("Rua C", "300", null, "Centro", "Campinas", "SP", "13010010"),
                null,
                null,
                new Cnpj("64245654000168"),
                List.of(),
                List.of(),
                4.1
        );

        ServicoEntity servicoEntity = new ServicoEntity("CORTE", Duration.ofMinutes(30));
        servicoEntity.setId(31L);

        ProfissionalEntity profissionalEntity = new ProfissionalEntity();
        profissionalEntity.setId(21L);
        profissionalEntity.setNome("Rafa");
        profissionalEntity.setEmail("rafa@teste.com");
        profissionalEntity.setCelular("11955554444");
        profissionalEntity.setUrlFoto("https://cdn/rafa.jpg");

        ServicoProfissionalEntity spEntity = new ServicoProfissionalEntity();
        spEntity.setServico(servicoEntity);
        spEntity.setProfissional(profissionalEntity);
        spEntity.setValor(new BigDecimal("70.00"));

        EstabelecimentoSearchResponseDto response = EstabelecimentoMapper.toResponseForSearch(
                estabelecimento,
                List.of(spEntity),
                repositoryJpa
        );

        assertNotNull(response);
        assertEquals(1, response.getServicosOfertados().size());
        assertEquals(4.3, response.getServicosOfertados().get(0).getProfissional().getNota());
        verify(repositoryJpa).findAverageProfissionalNota(21L);
    }

    @Test
    void deveMapearDominioParaEntidadeEDepoisParaDominioFiltrandoHorarioInvalido() {
        Estabelecimento domain = new Estabelecimento(
                10L,
                "Studio Roundtrip",
                new Endereco("Rua D", "400", "Casa", "Centro", "Santos", "SP", "11010020"),
                null,
                null,
                new Cnpj("64245654000168"),
                List.of("https://cdn/round.jpg"),
                List.of(new HorarioFuncionamento(null, DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(19, 0), false)),
                4.0
        );

        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntity(domain);
        assertNotNull(entity);
        assertEquals("64245654000168", entity.getCnpj());

        HorarioFuncionamentoEmbeddable horarioInvalido = new HorarioFuncionamentoEmbeddable();
        horarioInvalido.setDiaSemana(" ");
        horarioInvalido.setAbertura(LocalTime.of(8, 0));
        horarioInvalido.setFechamento(LocalTime.of(17, 0));
        horarioInvalido.setFechado(false);

        HorarioFuncionamentoEmbeddable horarioValido = new HorarioFuncionamentoEmbeddable();
        horarioValido.setDiaSemana("MONDAY");
        horarioValido.setAbertura(LocalTime.of(9, 0));
        horarioValido.setFechamento(LocalTime.of(18, 0));
        horarioValido.setFechado(false);

        EstabelecimentoEntity entityComHorarioMisto = new EstabelecimentoEntity();
        entityComHorarioMisto.setId(10L);
        entityComHorarioMisto.setNome("Studio Roundtrip");
        entityComHorarioMisto.setEndereco(new EnderecoEntity("Rua D", "400", "Casa", "Centro", "Santos", "SP", "11010020"));
        entityComHorarioMisto.setCnpj("64245654000168");
        entityComHorarioMisto.setUrlFotos(List.of("https://cdn/round.jpg"));
        entityComHorarioMisto.setHorarioFuncionamento(List.of(horarioValido, horarioInvalido));
        entityComHorarioMisto.setNota(4.0);

        Estabelecimento mappedBack = EstabelecimentoMapper.toDomain(entityComHorarioMisto);

        assertNotNull(mappedBack);
        assertEquals(1, mappedBack.getHorarioFuncionamento().size());
        assertEquals(DayOfWeek.MONDAY, mappedBack.getHorarioFuncionamento().get(0).getDiaSemana());
    }

    @Test
    void deveRetornarNuloParaEntradasNulas() {
        assertNull(EstabelecimentoMapper.toDomain((EstabelecimentoDto) null));
        assertNull(EstabelecimentoMapper.toResponse((Estabelecimento) null));
        assertNull(EstabelecimentoMapper.toEntity(null));
        assertNull(EstabelecimentoMapper.toDomain((EstabelecimentoEntity) null));
        assertNull(EstabelecimentoMapper.toResponseForSearch((Estabelecimento) null));
        assertNull(EstabelecimentoMapper.toResponseForSearch(null, List.of(), mock(EstabelecimentoRepositoryJpa.class)));
    }
}
