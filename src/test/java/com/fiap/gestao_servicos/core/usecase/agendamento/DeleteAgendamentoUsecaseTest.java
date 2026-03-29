package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteAgendamentoUseCaseTest {

    @Test
    void deveExcluirAgendamentoQuandoStatusAgendado() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        DeleteAgendamentoUseCase UseCase = new DeleteAgendamentoUseCase(repository);
        Agendamento agendamento = mock(Agendamento.class);

        when(repository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.AGENDADO);

        UseCase.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deveExcluirAgendamentoQuandoStatusCancelado() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        DeleteAgendamentoUseCase UseCase = new DeleteAgendamentoUseCase(repository);
        Agendamento agendamento = mock(Agendamento.class);

        when(repository.findById(2L)).thenReturn(Optional.of(agendamento));
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.CANCELADO);

        UseCase.deleteById(2L);

        verify(repository).deleteById(2L);
    }

    @Test
    void deveLancarExcecaoQuandoStatusConcluido() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        DeleteAgendamentoUseCase UseCase = new DeleteAgendamentoUseCase(repository);
        Agendamento agendamento = mock(Agendamento.class);

        when(repository.findById(3L)).thenReturn(Optional.of(agendamento));
        when(agendamento.getStatus()).thenReturn(AgendamentoStatus.CONCLUIDO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> UseCase.deleteById(3L));

        assertEquals("Nao e permitido excluir agendamento com status CONCLUIDO.", ex.getMessage());
        verify(repository, never()).deleteById(3L);
    }

    @Test
    void deveLancarNotFoundQuandoAgendamentoNaoExistir() {
        AgendamentoRepository repository = mock(AgendamentoRepository.class);
        DeleteAgendamentoUseCase UseCase = new DeleteAgendamentoUseCase(repository);

        when(repository.findById(4L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> UseCase.deleteById(4L));

        assertTrue(ex.getMessage().contains("Agendamento"));
        assertTrue(ex.getMessage().contains("4"));
        verify(repository, never()).deleteById(4L);
    }
}


