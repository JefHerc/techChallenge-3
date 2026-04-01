package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllAgendamentosUseCaseTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    private FindAllAgendamentosUseCase useCase;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-31T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
        useCase = new FindAllAgendamentosUseCase(agendamentoRepository, fixedClock);
    }

    @Test
    void deveUsarPeriodoDe30DiasAPartirDaDataAtualQuandoParametrosNaoForemInformados() {
        when(agendamentoRepository.findByEstabelecimentoIdAndDataHoraInicioBetween(
                eq(1L),
                eq(LocalDateTime.of(2026, 3, 31, 0, 0)),
                eq(LocalDateTime.of(2026, 4, 30, 23, 59, 59, 999_999_999))))
                .thenReturn(List.of());

        List<Agendamento> resultado = useCase.findByEstabelecimentoIdAndPeriodo(1L, null, null);

        assertEquals(0, resultado.size());
        verify(agendamentoRepository).findByEstabelecimentoIdAndDataHoraInicioBetween(
                1L,
                LocalDateTime.of(2026, 3, 31, 0, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59, 59, 999_999_999));
    }

    @Test
    void deveUsarPeriodoInformadoQuandoDatasForemEnviadas() {
        when(agendamentoRepository.findByEstabelecimentoIdAndDataHoraInicioBetween(
                eq(1L),
                eq(LocalDateTime.of(2026, 4, 10, 0, 0)),
                eq(LocalDateTime.of(2026, 4, 12, 23, 59, 59, 999_999_999))))
                .thenReturn(List.of());

        List<Agendamento> resultado = useCase.findByEstabelecimentoIdAndPeriodo(
                1L,
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 12));

        assertEquals(0, resultado.size());
        verify(agendamentoRepository).findByEstabelecimentoIdAndDataHoraInicioBetween(
                1L,
                LocalDateTime.of(2026, 4, 10, 0, 0),
                LocalDateTime.of(2026, 4, 12, 23, 59, 59, 999_999_999));
    }

    @Test
    void deveLancarExcecaoQuandoDataFinalForAnteriorADataInicial() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.findByEstabelecimentoIdAndPeriodo(
                        1L,
                        LocalDate.of(2026, 4, 12),
                        LocalDate.of(2026, 4, 10)));

        assertEquals("dataFinal deve ser maior ou igual à dataInicial", ex.getMessage());
    }
}
