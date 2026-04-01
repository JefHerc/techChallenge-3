package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoProfissional;
import com.fiap.gestao_servicos.core.exception.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VerifyProfissionalVinculoUseCaseTest {

    private VerifyProfissionalVinculoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new VerifyProfissionalVinculoUseCase();
    }

    @Test
    void devePassarQuandoExpedienteEstaoDentroDoHorario() {
        HorarioFuncionamento horario = new HorarioFuncionamento(null, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(18, 0), false);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getHorarioFuncionamento()).thenReturn(List.of(horario));
        when(estabelecimento.getServicos()).thenReturn(List.of());

        ExpedienteProfissional expediente = mock(ExpedienteProfissional.class);
        when(expediente.getDiaSemana()).thenReturn(DayOfWeek.MONDAY);
        when(expediente.getInicioTurno()).thenReturn(LocalTime.of(9, 0));
        when(expediente.getFimTurno()).thenReturn(LocalTime.of(17, 0));
        when(expediente.getInicioIntervalo()).thenReturn(null);
        when(expediente.getFimIntervalo()).thenReturn(null);

        Profissional profissional = mock(Profissional.class);
        when(profissional.getExpedientes()).thenReturn(List.of(expediente));
        when(profissional.getServicosProfissional()).thenReturn(List.of());

        assertDoesNotThrow(() -> useCase.verify(estabelecimento, profissional));
    }

    @Test
    void deveLancarExcecaoQuandoExpedienteForaDoHorario() {
        HorarioFuncionamento horario = new HorarioFuncionamento(null, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(18, 0), false);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getHorarioFuncionamento()).thenReturn(List.of(horario));
        when(estabelecimento.getServicos()).thenReturn(List.of());

        ExpedienteProfissional expediente = mock(ExpedienteProfissional.class);
        when(expediente.getDiaSemana()).thenReturn(DayOfWeek.MONDAY);
        when(expediente.getInicioTurno()).thenReturn(LocalTime.of(7, 0));
        when(expediente.getFimTurno()).thenReturn(LocalTime.of(17, 0));
        when(expediente.getInicioIntervalo()).thenReturn(null);
        when(expediente.getFimIntervalo()).thenReturn(null);

        Profissional profissional = mock(Profissional.class);
        when(profissional.getExpedientes()).thenReturn(List.of(expediente));
        when(profissional.getServicosProfissional()).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> useCase.verify(estabelecimento, profissional));
    }

    @Test
    void deveLancarExcecaoQuandoDiaNaoConfiguradoNoEstabelecimento() {
        HorarioFuncionamento horarioSegunda = new HorarioFuncionamento(null, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(18, 0), false);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getHorarioFuncionamento()).thenReturn(List.of(horarioSegunda));
        when(estabelecimento.getServicos()).thenReturn(List.of());

        ExpedienteProfissional expediente = mock(ExpedienteProfissional.class);
        when(expediente.getDiaSemana()).thenReturn(DayOfWeek.TUESDAY);
        when(expediente.getInicioTurno()).thenReturn(LocalTime.of(9, 0));
        when(expediente.getFimTurno()).thenReturn(LocalTime.of(17, 0));
        when(expediente.getInicioIntervalo()).thenReturn(null);
        when(expediente.getFimIntervalo()).thenReturn(null);

        Profissional profissional = mock(Profissional.class);
        when(profissional.getExpedientes()).thenReturn(List.of(expediente));
        when(profissional.getServicosProfissional()).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> useCase.verify(estabelecimento, profissional));
    }

    @Test
    void deveLancarExcecaoQuandoServicoNaoPertenceAoEstabelecimento() {
        Servico servicoDoEstabelecimento = mock(Servico.class);
        when(servicoDoEstabelecimento.getId()).thenReturn(10L);

        Servico servicoForaDoEstabelecimento = mock(Servico.class);
        when(servicoForaDoEstabelecimento.getId()).thenReturn(99L);

        ServicoProfissional servicoProfissional = mock(ServicoProfissional.class);
        when(servicoProfissional.getServico()).thenReturn(servicoForaDoEstabelecimento);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getHorarioFuncionamento()).thenReturn(List.of());
        when(estabelecimento.getServicos()).thenReturn(List.of(servicoDoEstabelecimento));

        Profissional profissional = mock(Profissional.class);
        when(profissional.getExpedientes()).thenReturn(List.of());
        when(profissional.getServicosProfissional()).thenReturn(List.of(servicoProfissional));

        assertThrows(BusinessRuleException.class, () -> useCase.verify(estabelecimento, profissional));
    }

    @Test
    void devePassarQuandoServicoPertenceAoEstabelecimento() {
        Servico servico = mock(Servico.class);
        when(servico.getId()).thenReturn(10L);

        ServicoProfissional servicoProfissional = mock(ServicoProfissional.class);
        when(servicoProfissional.getServico()).thenReturn(servico);

        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getHorarioFuncionamento()).thenReturn(List.of());
        when(estabelecimento.getServicos()).thenReturn(List.of(servico));

        Profissional profissional = mock(Profissional.class);
        when(profissional.getExpedientes()).thenReturn(List.of());
        when(profissional.getServicosProfissional()).thenReturn(List.of(servicoProfissional));

        assertDoesNotThrow(() -> useCase.verify(estabelecimento, profissional));
    }
}
