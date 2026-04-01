package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.exception.BusinessRuleException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import com.fiap.gestao_servicos.core.usecase.agendamento.input.AgendamentoInput;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateAgendamentoUseCaseTest {

    @Test
    void deveLancarExcecaoQuandoEstabelecimentoFechadoNoDia() {
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);
        ServicoRepository servicoRepository = mock(ServicoRepository.class);
        ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
        ClienteRepository clienteRepository = mock(ClienteRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);
        
        UpdateAgendamentoUseCase UseCase = new UpdateAgendamentoUseCase(
                agendamentoRepository,
                estabelecimentoRepository,
                servicoRepository,
                profissionalRepository,
                clienteRepository,
                notificationPort);
        
        AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                LocalDateTime.of(2026, 3, 30, 10, 0), AgendamentoStatus.AGENDADO);

        Profissional profissional = mock(Profissional.class);
        Servico servico = mock(Servico.class);
        Estabelecimento estabelecimento = new Estabelecimento(
                3L, "Salao", null, null, null, null, null,
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, null, null, true))
        );
        Cliente cliente = mock(Cliente.class);
        Agendamento agendamentoAtual = mock(Agendamento.class);

        when(agendamentoRepository.findById(7L)).thenReturn(Optional.of(agendamentoAtual));
        when(profissionalRepository.findByIdAndEstabelecimentoId(1L, 3L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findByIdAndEstabelecimentoId(2L, 3L)).thenReturn(Optional.of(servico));
        when(estabelecimentoRepository.findById(3L)).thenReturn(Optional.of(estabelecimento));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(cliente));
        when(profissional.getId()).thenReturn(1L);
        when(servico.getId()).thenReturn(2L);
        when(servico.getDuracaoMedia()).thenReturn(Duration.ofMinutes(30));
        when(profissionalRepository.existsVinculoProfissionalServico(1L, 2L)).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> UseCase.update(7L, input));

        assertEquals("Estabelecimento fechado para o dia informado", ex.getMessage());
    }

    @Test
    void deveAtualizarQuandoHorarioEstiverDentroFuncionamento() {
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);
        ServicoRepository servicoRepository = mock(ServicoRepository.class);
        ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
        ClienteRepository clienteRepository = mock(ClienteRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);
        
        UpdateAgendamentoUseCase UseCase = new UpdateAgendamentoUseCase(
                agendamentoRepository,
                estabelecimentoRepository,
                servicoRepository,
                profissionalRepository,
                clienteRepository,
                notificationPort);
        
        AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                LocalDateTime.of(2026, 3, 30, 9, 0), AgendamentoStatus.AGENDADO);

        Profissional profissional = mock(Profissional.class);
        Servico servico = mock(Servico.class);
        Estabelecimento estabelecimento = new Estabelecimento(
                3L, "Salao", null, null, null, null, null,
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );
        Cliente cliente = mock(Cliente.class);
        Agendamento agendamento = mock(Agendamento.class);
        Agendamento agendamentoAtual = mock(Agendamento.class);

        when(agendamentoRepository.findById(8L)).thenReturn(Optional.of(agendamentoAtual));
        when(profissionalRepository.findByIdAndEstabelecimentoId(1L, 3L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findByIdAndEstabelecimentoId(2L, 3L)).thenReturn(Optional.of(servico));
        when(estabelecimentoRepository.findById(3L)).thenReturn(Optional.of(estabelecimento));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(cliente));
        when(profissional.getId()).thenReturn(1L);
        when(servico.getId()).thenReturn(2L);
        when(servico.getDuracaoMedia()).thenReturn(Duration.ofMinutes(45));
        when(profissionalRepository.existsVinculoProfissionalServico(1L, 2L)).thenReturn(true);
        when(agendamentoRepository.existsConflitoHorarioProfissional(1L, 2L, input.getDataHoraInicio(), 8L)).thenReturn(false);
        when(agendamentoRepository.update(eq(8L), any(Agendamento.class))).thenReturn(agendamento);
                when(agendamentoAtual.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);
                when(agendamento.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);

        Agendamento atualizado = UseCase.update(8L, input);

        assertEquals(agendamento, atualizado);
        verify(agendamentoRepository).update(eq(8L), any(Agendamento.class));
                verify(notificationPort, never()).send(any(LembreteEvento.class));
    }

        @Test
        void deveLancarExcecaoQuandoProfissionalNaoPertencerAoEstabelecimento() {
                AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
                EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);
                ServicoRepository servicoRepository = mock(ServicoRepository.class);
                ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
                ClienteRepository clienteRepository = mock(ClienteRepository.class);
                NotificationPort notificationPort = mock(NotificationPort.class);

                UpdateAgendamentoUseCase useCase = new UpdateAgendamentoUseCase(
                                agendamentoRepository,
                                estabelecimentoRepository,
                                servicoRepository,
                                profissionalRepository,
                                clienteRepository,
                                notificationPort);

                AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                                LocalDateTime.of(2026, 3, 30, 10, 0), AgendamentoStatus.AGENDADO);

                when(agendamentoRepository.findById(7L)).thenReturn(Optional.of(mock(Agendamento.class)));
                when(profissionalRepository.findByIdAndEstabelecimentoId(1L, 3L)).thenReturn(Optional.empty());

                ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> useCase.update(7L, input));

                assertEquals("Profissional não encontrado para o id: 1", ex.getMessage());
                verify(servicoRepository, never()).findByIdAndEstabelecimentoId(any(), any());
        }

        @Test
        void deveLancarExcecaoQuandoServicoNaoPertencerAoEstabelecimento() {
                AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
                EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);
                ServicoRepository servicoRepository = mock(ServicoRepository.class);
                ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
                ClienteRepository clienteRepository = mock(ClienteRepository.class);
                NotificationPort notificationPort = mock(NotificationPort.class);
                Profissional profissional = mock(Profissional.class);

                UpdateAgendamentoUseCase useCase = new UpdateAgendamentoUseCase(
                                agendamentoRepository,
                                estabelecimentoRepository,
                                servicoRepository,
                                profissionalRepository,
                                clienteRepository,
                                notificationPort);

                AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                                LocalDateTime.of(2026, 3, 30, 10, 0), AgendamentoStatus.AGENDADO);

                when(agendamentoRepository.findById(7L)).thenReturn(Optional.of(mock(Agendamento.class)));
                when(profissionalRepository.findByIdAndEstabelecimentoId(1L, 3L)).thenReturn(Optional.of(profissional));
                when(servicoRepository.findByIdAndEstabelecimentoId(2L, 3L)).thenReturn(Optional.empty());

                ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> useCase.update(7L, input));

                assertEquals("Serviço não encontrado para o id: 2", ex.getMessage());
                verify(estabelecimentoRepository, never()).findById(any());
        }

    @Test
    void deveEnviarAvisosQuandoStatusMudarParaCancelado() {
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);
        ServicoRepository servicoRepository = mock(ServicoRepository.class);
        ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
        ClienteRepository clienteRepository = mock(ClienteRepository.class);
        NotificationPort notificationPort = mock(NotificationPort.class);

        UpdateAgendamentoUseCase useCase = new UpdateAgendamentoUseCase(
                agendamentoRepository,
                estabelecimentoRepository,
                servicoRepository,
                profissionalRepository,
                clienteRepository,
                notificationPort);

        AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                LocalDateTime.of(2026, 3, 30, 9, 0), AgendamentoStatus.CANCELADO);

        Profissional profissional = mock(Profissional.class);
        Servico servico = mock(Servico.class);
        Cliente cliente = mock(Cliente.class);
        Estabelecimento estabelecimento = new Estabelecimento(
                3L, "Salao", null, null, null, null, null,
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );
        Agendamento agendamentoAtual = mock(Agendamento.class);
        Agendamento atualizado = mock(Agendamento.class);

        when(agendamentoRepository.findById(8L)).thenReturn(Optional.of(agendamentoAtual));
        when(profissionalRepository.findByIdAndEstabelecimentoId(1L, 3L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findByIdAndEstabelecimentoId(2L, 3L)).thenReturn(Optional.of(servico));
        when(estabelecimentoRepository.findById(3L)).thenReturn(Optional.of(estabelecimento));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(cliente));
        when(profissional.getId()).thenReturn(1L);
        when(servico.getId()).thenReturn(2L);
        when(servico.getDuracaoMedia()).thenReturn(Duration.ofMinutes(45));
        when(profissionalRepository.existsVinculoProfissionalServico(1L, 2L)).thenReturn(true);
        when(agendamentoRepository.update(eq(8L), any(Agendamento.class))).thenReturn(atualizado);
        when(agendamentoAtual.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);
        when(atualizado.getStatus()).thenReturn(AgendamentoStatus.CANCELADO);
        when(atualizado.getId()).thenReturn(8L);
        when(atualizado.getCliente()).thenReturn(cliente);
        when(atualizado.getProfissional()).thenReturn(profissional);
        when(cliente.getEmail()).thenReturn(new com.fiap.gestao_servicos.core.domain.Email("joao@cliente.com"));
        when(profissional.getEmail()).thenReturn(new com.fiap.gestao_servicos.core.domain.Email("bruno@e1.com"));

        useCase.update(8L, input);

        ArgumentCaptor<LembreteEvento> eventoCaptor = ArgumentCaptor.forClass(LembreteEvento.class);
        verify(notificationPort, times(2)).send(eventoCaptor.capture());
        assertEquals(2, eventoCaptor.getAllValues().size());
        assertEquals("joao@cliente.com", eventoCaptor.getAllValues().get(0).getDestinoEmail());
        assertNull(eventoCaptor.getAllValues().get(0).getTipo());
        assertEquals("bruno@e1.com", eventoCaptor.getAllValues().get(1).getDestinoEmail());
    }
}


