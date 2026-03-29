package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FindAgendamentosParaLembreteUseCaseTest {

    @Test
    void deveRetornarAgendamentosNaJanela() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        FindAgendamentosParaLembreteUseCase useCase = new FindAgendamentosParaLembreteUseCase(repository);

        LocalDateTime inicio = LocalDateTime.now().plusHours(1);
        LocalDateTime fim = inicio.plusMinutes(1);
        List<Agendamento> esperados = List.of(mock(Agendamento.class));

        when(repository.findByStatusAndDataHoraInicioBetween(AgendamentoStatus.AGENDADO, inicio, fim))
                .thenReturn(esperados);

        List<Agendamento> resultado = useCase.findByJanela(AgendamentoStatus.AGENDADO, inicio, fim);

        assertEquals(esperados, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoStatusNulo() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        FindAgendamentosParaLembreteUseCase useCase = new FindAgendamentosParaLembreteUseCase(repository);

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(1);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.findByJanela(null, inicio, fim));
    }

    @Test
    void deveLancarExcecaoQuandoInicioNulo() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        FindAgendamentosParaLembreteUseCase useCase = new FindAgendamentosParaLembreteUseCase(repository);

        LocalDateTime fim = LocalDateTime.now().plusMinutes(1);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.findByJanela(AgendamentoStatus.AGENDADO, null, fim));
    }

    @Test
    void deveLancarExcecaoQuandoInicioNaoAntesDeFim() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        FindAgendamentosParaLembreteUseCase useCase = new FindAgendamentosParaLembreteUseCase(repository);

        LocalDateTime momento = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class,
                () -> useCase.findByJanela(AgendamentoStatus.AGENDADO, momento, momento));
    }
}
