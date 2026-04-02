package com.fiap.gestao_servicos.infrastructure.mapper.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.ServicoProfissional;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ExpedienteProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalSearchResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ServicoProfissionalDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProfissionalMapperTest {

    @Test
    void deveRetornarNuloQuandoToDomainReceberNulo() {
        assertNull(ProfissionalMapper.toDomain(null));
    }

    @Test
    void deveMapearDtoParaDominioComSexoExpedientesEServicos() {
        ProfissionalDto dto = new ProfissionalDto();
        dto.setNome("Carla Souza");
        dto.setCpf("52998224725");
        dto.setCelular("11988887777");
        dto.setEmail("carla@teste.com");
        dto.setUrlFoto("https://cdn/foto.jpg");
        dto.setDescricao("Especialista");
        dto.setSexo("feminino");

        ExpedienteProfissionalDto expediente = new ExpedienteProfissionalDto();
        expediente.setDiaSemana("segunda-feira");
        expediente.setInicioTurno("08:00");
        expediente.setFimTurno("17:00");
        expediente.setInicioIntervalo("12:00");
        expediente.setFimIntervalo("13:00");

        ServicoProfissionalDto servico = new ServicoProfissionalDto();
        servico.setServicoId(5L);
        servico.setValor(new BigDecimal("90.00"));

        dto.setExpedientes(List.of(expediente));
        dto.setServicosProfissional(List.of(servico));

        Profissional profissional = ProfissionalMapper.toDomain(dto);

        assertNotNull(profissional);
        assertEquals("Carla Souza", profissional.getNome());
        assertEquals("52998224725", profissional.getCpf().getValue());
        assertEquals(Sexo.FEMININO, profissional.getSexo());
        assertEquals(1, profissional.getExpedientes().size());
        assertEquals(DayOfWeek.MONDAY, profissional.getExpedientes().get(0).getDiaSemana());
        assertEquals(1, profissional.getServicosProfissional().size());
        assertEquals(5L, profissional.getServicosProfissional().get(0).getServico().getId());
    }

    @Test
    void deveMapearDominioParaResponseComCamposNulosEListas() {
        ExpedienteProfissional expediente = new ExpedienteProfissional(
                null,
                DayOfWeek.TUESDAY,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                null,
                null
        );

        ServicoProfissional servicoSemDetalhe = new ServicoProfissional(null, null, null, new BigDecimal("45.50"));

        Profissional profissional = new Profissional(
                22L,
                "Joao",
                new Cpf("06854809096"),
                new Celular("11977776666"),
                new Email("joao@teste.com"),
                "https://cdn/joao.jpg",
                "Descricao",
                List.of(expediente),
                null,
                List.of(servicoSemDetalhe)
        );

        ProfissionalResponseDto response = ProfissionalMapper.toResponse(profissional);

        assertNotNull(response);
        assertEquals(22L, response.getId());
        assertEquals("Joao", response.getNome());
        assertNull(response.getSexo());
        assertEquals(1, response.getExpedientes().size());
        assertEquals("terca-feira", response.getExpedientes().get(0).getDiaSemana());
        assertEquals(1, response.getServicosProfissional().size());
        assertNull(response.getServicosProfissional().get(0).getServicoId());
        assertNull(response.getServicosProfissional().get(0).getServicoNome());
    }

    @Test
    void deveMapearDominioParaResponseDeBusca() {
        Profissional profissional = new Profissional(
                33L,
                "Ana",
                new Cpf("06854809096"),
                new Celular("11966665555"),
                new Email("ana@teste.com"),
                "https://cdn/ana.jpg",
                "Cabeleireira",
                null,
                Sexo.FEMININO,
                null
        );

        ProfissionalSearchResponseDto response = ProfissionalMapper.toResponseForSearch(profissional);

        assertNotNull(response);
        assertEquals(33L, response.getId());
        assertEquals("Ana", response.getNome());
        assertEquals("https://cdn/ana.jpg", response.getUrlFoto());
        assertEquals("Cabeleireira", response.getDescricao());
        assertNull(ProfissionalMapper.toResponseForSearch(null));
    }
}
