package com.fiap.gestao_servicos.infrastructure.scheduler;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import com.fiap.gestao_servicos.core.repository.LembreteEventoRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentosParaLembreteUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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

        job.processarLembretes();

        verify(findUseCase, times(3)).findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void deveUtilizarMinusDelayQuandoUltimaExecucaoNula() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        ReflectionTestUtils.setField(job, "ultimaExecucao", null);

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());

        job.processarLembretes();

        verify(findUseCase, times(3)).findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void deveCriarLembreteParaCLIENTEQuandoClienteTemEmail() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        // Mock Agendamento
        Agendamento agendamento = mock(Agendamento.class);
        when(agendamento.getId()).thenReturn(1L);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);

        // Mock Cliente with email
        Cliente cliente = mock(Cliente.class);
        when(cliente.getEmail()).thenReturn(new Email("cliente@teste.com"));
        when(agendamento.getCliente()).thenReturn(cliente);

        // Mock Profissional without email
        Profissional profissional = mock(Profissional.class);
        when(profissional.getEmail()).thenReturn(null);
        when(agendamento.getProfissional()).thenReturn(profissional);

        // Mock Servico
        Servico servico = mock(Servico.class);
        when(servico.getNomeDescricao()).thenReturn("CORTE");
        when(agendamento.getServico()).thenReturn(servico);

        // Mock Estabelecimento
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getNome()).thenReturn("Salão Exemplo");
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);

        LocalDateTime agora = LocalDateTime.now();
        when(agendamento.getDataHoraInicio()).thenReturn(agora.plusHours(24));

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento));
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());
        when(lembreteEventoRepository.existsByAgendamentoIdAndTipoAndDestinatario(any(), any(), any()))
                .thenReturn(false);

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(lembreteEventoRepository, times(6)).save(any(LembreteEvento.class));
    }

    @Test
    void deveCriarLembreteParaPROFISSIONALQuandoProfissionalTemEmail() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        // Mock Agendamento
        Agendamento agendamento = mock(Agendamento.class);
        when(agendamento.getId()).thenReturn(2L);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);

        // Mock Cliente without email
        Cliente cliente = mock(Cliente.class);
        when(cliente.getEmail()).thenReturn(null);
        when(agendamento.getCliente()).thenReturn(cliente);

        // Mock Profissional with email
        Profissional profissional = mock(Profissional.class);
        when(profissional.getEmail()).thenReturn(new Email("prof@teste.com"));
        when(agendamento.getProfissional()).thenReturn(profissional);

        // Mock Servico
        Servico servico = mock(Servico.class);
        when(servico.getNomeDescricao()).thenReturn("CORTE");
        when(agendamento.getServico()).thenReturn(servico);

        // Mock Estabelecimento
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getNome()).thenReturn("Salão Exemplo");
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);

        LocalDateTime agora = LocalDateTime.now();
        when(agendamento.getDataHoraInicio()).thenReturn(agora.plusHours(24));

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento));
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());
        when(lembreteEventoRepository.existsByAgendamentoIdAndTipoAndDestinatario(any(), any(), any()))
                .thenReturn(false);

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(lembreteEventoRepository, times(6)).save(any(LembreteEvento.class));
    }

    @Test
    void deveOmitirEmailQuandoClienteNulo() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        // Mock Agendamento
        Agendamento agendamento = mock(Agendamento.class);
        when(agendamento.getId()).thenReturn(3L);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);

        // Cliente is null
        when(agendamento.getCliente()).thenReturn(null);

        // Mock Profissional
        Profissional profissional = mock(Profissional.class);
        when(profissional.getEmail()).thenReturn(new Email("prof@teste.com"));
        when(agendamento.getProfissional()).thenReturn(profissional);

        Servico servico = mock(Servico.class);
        when(servico.getNomeDescricao()).thenReturn("CORTE");
        when(agendamento.getServico()).thenReturn(servico);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getNome()).thenReturn("Salão Exemplo");
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);

        LocalDateTime agora = LocalDateTime.now();
        when(agendamento.getDataHoraInicio()).thenReturn(agora.plusHours(24));

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento));
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());
        when(lembreteEventoRepository.existsByAgendamentoIdAndTipoAndDestinatario(any(), any(), any()))
                .thenReturn(false);

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(lembreteEventoRepository, times(6)).save(any(LembreteEvento.class));
    }

    @Test
    void deveOmitirEmailQuandoProfissionalNulo() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        // Mock Agendamento
        Agendamento agendamento = mock(Agendamento.class);
        when(agendamento.getId()).thenReturn(4L);
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);

        // Mock Cliente
        Cliente cliente = mock(Cliente.class);
        when(cliente.getEmail()).thenReturn(new Email("cliente@teste.com"));
        when(agendamento.getCliente()).thenReturn(cliente);

        // Profissional is null
        when(agendamento.getProfissional()).thenReturn(null);

        Servico servico = mock(Servico.class);
        when(servico.getNomeDescricao()).thenReturn("CORTE");
        when(agendamento.getServico()).thenReturn(servico);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getNome()).thenReturn("Salão Exemplo");
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);

        LocalDateTime agora = LocalDateTime.now();
        when(agendamento.getDataHoraInicio()).thenReturn(agora.plusHours(24));

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento));
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());
        when(lembreteEventoRepository.existsByAgendamentoIdAndTipoAndDestinatario(any(), any(), any()))
                .thenReturn(false);

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(lembreteEventoRepository, times(6)).save(any(LembreteEvento.class));
    }

    @Test
    void deveNaoEnviarQuandoLembreteJaExiste() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        Agendamento agendamento = mock(Agendamento.class);
        when(agendamento.getId()).thenReturn(5L);

        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(agendamento));
        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of());
        when(lembreteEventoRepository.existsByAgendamentoIdAndTipoAndDestinatario(any(), any(), any()))
                .thenReturn(true);

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        // Should not save if reminder already exists
        verify(lembreteEventoRepository, times(0)).save(any(LembreteEvento.class));
    }

    @Test
    void deveEnviarLembretesPendentes() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        LembreteEvento lembrete = mock(LembreteEvento.class);
        when(lembrete.marcarComoEnviado(any(LocalDateTime.class))).thenReturn(lembrete);

        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of(lembrete));
        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(notificationPort, times(1)).send(eq(lembrete));
        verify(lembreteEventoRepository, times(1)).save(any(LembreteEvento.class));
    }

    @Test
    void deveMarcarComoFalhaAoErrorNoEnvio() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        LembreteEvento lembrete = mock(LembreteEvento.class);
        when(lembrete.marcarComoFalha(any(String.class))).thenReturn(lembrete);

        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of(lembrete));
        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        doThrow(new RuntimeException("Erro ao enviar")).when(notificationPort).send(eq(lembrete));

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(notificationPort, times(1)).send(eq(lembrete));
        verify(lembreteEventoRepository, times(1)).save(any(LembreteEvento.class));
    }

    @Test
    void deveProcessarMultiplosPendentesAteCoisaLimite() {
        FindAgendamentosParaLembreteUseCase findUseCase = mock(FindAgendamentosParaLembreteUseCase.class);
        LembreteEventoRepository lembreteEventoRepository = mock(LembreteEventoRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        LembreteEvento lembrete1 = mock(LembreteEvento.class);
        LembreteEvento lembrete2 = mock(LembreteEvento.class);
        LembreteEvento lembrete3 = mock(LembreteEvento.class);

        when(lembrete1.marcarComoEnviado(any(LocalDateTime.class))).thenReturn(lembrete1);
        when(lembrete2.marcarComoEnviado(any(LocalDateTime.class))).thenReturn(lembrete2);
        when(lembrete3.marcarComoEnviado(any(LocalDateTime.class))).thenReturn(lembrete3);

        when(lembreteEventoRepository.findTop200Pendentes()).thenReturn(List.of(lembrete1, lembrete2, lembrete3));
        when(findUseCase.findByJanela(eq(AgendamentoStatus.AGENDADO), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        LembreteAgendamentoJob job = new LembreteAgendamentoJob(
                findUseCase,
                lembreteEventoRepository,
                notificationPort,
                60_000L);

        job.processarLembretes();

        verify(notificationPort, times(3)).send(any(LembreteEvento.class));
        verify(lembreteEventoRepository, times(3)).save(any(LembreteEvento.class));
    }
}

