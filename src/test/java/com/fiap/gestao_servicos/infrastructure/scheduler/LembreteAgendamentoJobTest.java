package com.fiap.gestao_servicos.infrastructure.scheduler;

import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.repository.LembreteEventoRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentosParaLembreteUseCase;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LembreteAgendamentoJobTest {

    @Test
    void deveBuscarLembretesComJanelaAmplaParaTodosOsTipos() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        LocalDateTime ultimaExecucao = LocalDateTime.now().minusMinutes(1);
        ReflectionTestUtils.setField(job, "ultimaExecucao", ultimaExecucao);

        job.processarLembretes();

        ArgumentCaptor<LocalDateTime> inicioCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> fimCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(findUseCase, times(3))
            .findByJanela(eq(AgendamentoStatus.AGENDADO), inicioCaptor.capture(), fimCaptor.capture());

        LocalDateTime inicioT24 = inicioCaptor.getAllValues().get(0);
        LocalDateTime fimT24 = fimCaptor.getAllValues().get(0);
        LocalDateTime inicioT2 = inicioCaptor.getAllValues().get(1);
        LocalDateTime fimT2 = fimCaptor.getAllValues().get(1);
        LocalDateTime inicioT30 = inicioCaptor.getAllValues().get(2);
        LocalDateTime fimT30 = fimCaptor.getAllValues().get(2);

        assertEquals(ultimaExecucao, inicioT24);
        assertTrue(Duration.between(inicioT24, fimT24).toMinutes() >= 24 * 60);
        assertEquals(ultimaExecucao, inicioT2);
        assertTrue(Duration.between(inicioT2, fimT2).toMinutes() >= 2 * 60);
        assertEquals(ultimaExecucao, inicioT30);
        assertTrue(Duration.between(inicioT30, fimT30).toMinutes() >= 30);
    }
}
