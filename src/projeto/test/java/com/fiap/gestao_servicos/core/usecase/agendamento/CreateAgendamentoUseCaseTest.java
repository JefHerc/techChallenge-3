package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoInput;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAgendamentoUseCaseTest {

    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private EstabelecimentoRepository estabelecimentoRepository;
    @Mock private ServicoRepository servicoRepository;
    @Mock private ProfissionalRepository profissionalRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private Profissional profissional;
    @Mock private Servico servico;
    @Mock private Cliente cliente;

    private CreateAgendamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateAgendamentoUseCase(
                agendamentoRepository,
                estabelecimentoRepository,
                servicoRepository,
                profissionalRepository,
                clienteRepository);
    }

    @Test
    void deveLancarExcecaoQuandoEstabelecimentoEstiverFechadoNoDia() {
        AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                LocalDateTime.of(2026, 3, 30, 10, 0), AgendamentoStatus.AGENDADO);

        Estabelecimento estabelecimento = new Estabelecimento(
                3L, "Salao", null, null, null, null, null,
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, null, null, true))
        );

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findById(2L)).thenReturn(Optional.of(servico));
        when(estabelecimentoRepository.findById(3L)).thenReturn(Optional.of(estabelecimento));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(cliente));
        when(profissional.getId()).thenReturn(1L);
        when(servico.getId()).thenReturn(2L);
        when(servico.getDuracaoMedia()).thenReturn(Duration.ofMinutes(60));
        when(profissionalRepository.existsVinculoProfissionalServico(1L, 2L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.create(input));

        assertEquals("Estabelecimento fechado para o dia informado", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoHorarioFinalUltrapassarFechamento() {
        AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                LocalDateTime.of(2026, 3, 30, 17, 30), AgendamentoStatus.AGENDADO);

        Estabelecimento estabelecimento = new Estabelecimento(
                3L, "Salao", null, null, null, null, null,
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findById(2L)).thenReturn(Optional.of(servico));
        when(estabelecimentoRepository.findById(3L)).thenReturn(Optional.of(estabelecimento));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(cliente));
        when(profissional.getId()).thenReturn(1L);
        when(servico.getId()).thenReturn(2L);
        when(servico.getDuracaoMedia()).thenReturn(Duration.ofMinutes(60));
        when(profissionalRepository.existsVinculoProfissionalServico(1L, 2L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.create(input));

        assertTrue(ex.getMessage().contains("agendamento fora do"));
        assertTrue(ex.getMessage().contains("funcionamento do estabelecimento"));
    }

    @Test
    void devePermitirQuandoEstiverDentroHorarioFuncionamento() {
        AgendamentoInput input = new AgendamentoInput(1L, 2L, 3L, 4L,
                LocalDateTime.of(2026, 3, 30, 17, 0), AgendamentoStatus.AGENDADO);

        Estabelecimento estabelecimento = new Estabelecimento(
                3L, "Salao", null, null, null, null, null,
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );
        Agendamento agendamento = org.mockito.Mockito.mock(Agendamento.class);

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findById(2L)).thenReturn(Optional.of(servico));
        when(estabelecimentoRepository.findById(3L)).thenReturn(Optional.of(estabelecimento));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(cliente));
        when(profissional.getId()).thenReturn(1L);
        when(servico.getId()).thenReturn(2L);
        when(servico.getDuracaoMedia()).thenReturn(Duration.ofMinutes(60));
        when(profissionalRepository.existsVinculoProfissionalServico(1L, 2L)).thenReturn(true);
        when(agendamentoRepository.existsConflitoHorarioProfissional(1L, 2L, input.getDataHoraInicio(), null)).thenReturn(false);
        when(agendamentoRepository.create(any(Agendamento.class))).thenReturn(agendamento);

        Agendamento created = useCase.create(input);

        assertEquals(agendamento, created);
        verify(agendamentoRepository).create(any(Agendamento.class));
    }
}



