package com.fiap.gestao_servicos.core.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgendaDomainTest {

    @Test
    void horarioFuncionamentoDeveAceitarDiaFechadoSemHorarios() {
        HorarioFuncionamento horario = new HorarioFuncionamento(null, DayOfWeek.MONDAY, null, null, true);

        assertTrue(horario.isFechado());
        assertEquals("segunda-feira: fechado", horario.toString());
        assertEquals("segunda-feira", horario.getDiaSemanaPt());
    }

    @Test
    void horarioFuncionamentoDeveGerarDescricaoTextualQuandoAberto() {
        HorarioFuncionamento horario = new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false);

        assertEquals("segunda-feira 08:00-18:00", horario.toString());
    }

    @Test
    void horarioFuncionamentoFromStringDeveRetornarDescricaoLivre() {
        HorarioFuncionamento horario = HorarioFuncionamento.fromString("Seg a Sex 08:00-18:00");

        assertEquals("Seg a Sex 08:00-18:00", horario.getDescricao());
        assertNull(horario.getDiaSemana());
        assertEquals("Seg a Sex 08:00-18:00", horario.toString());
    }

    @Test
    void horarioFuncionamentoDeveRejeitarFechamentoAntesDaAbertura() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(9, 0), false));

        assertEquals("Hora de fechamento deve ser depois da hora de abertura", ex.getMessage());
    }

    @Test
    void expedienteProfissionalDeveAceitarIntervaloValido() {
        ExpedienteProfissional expediente = new ExpedienteProfissional(
                null,
                DayOfWeek.TUESDAY,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0)
        );

        assertEquals(DayOfWeek.TUESDAY, expediente.getDiaSemana());
        assertEquals(LocalTime.of(12, 0), expediente.getInicioIntervalo());
        assertEquals(LocalTime.of(13, 0), expediente.getFimIntervalo());
    }

    @Test
    void expedienteProfissionalDeveRejeitarFimDeTurnoInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(9, 0), null, null));

        assertEquals("Hora de fim do turno deve ser depois da hora de início do turno", ex.getMessage());
    }

    @Test
    void expedienteProfissionalDeveRejeitarIntervaloForaDoTurno() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), LocalTime.of(8, 0), null));

        assertEquals("Hora de início do intervalo deve estar dentro do turno", ex.getMessage());
    }

    @Test
    void servicoDeveAceitarDuracaoPositivaEExporDescricao() {
        Servico servico = new Servico(1L, ServicoEnum.CORTE, Duration.ofMinutes(90));

        assertEquals(1L, servico.getId());
        assertEquals(ServicoEnum.CORTE, servico.getNome());
        assertEquals("CORTE", servico.getNomeAsString());
        assertEquals(ServicoEnum.CORTE.getDescricao(), servico.getNomeDescricao());
        assertEquals(Duration.ofMinutes(90), servico.getDuracaoMedia());
    }

    @Test
    void servicoDeveRejeitarDuracaoNaoPositiva() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Servico(ServicoEnum.CORTE, Duration.ZERO));

        assertEquals("Duração média deve ser positiva", ex.getMessage());
    }

    @Test
    void servicoProfissionalDeveAceitarValorZeroOuMaior() {
        ServicoProfissional servicoProfissional = new ServicoProfissional(null, null, null, BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, servicoProfissional.getValor());
    }

    @Test
    void servicoProfissionalDeveRejeitarValorNegativo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ServicoProfissional(null, null, null, new BigDecimal("-1.00")));

        assertEquals("Valor do serviço não pode ser negativo", ex.getMessage());
    }
}