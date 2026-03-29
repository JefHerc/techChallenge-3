package com.fiap.gestao_servicos.core.domain;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgendamentoAvaliacaoDomainTest {

    @Test
    void agendamentoDeveCalcularDataHoraFimComBaseNaDuracaoDoServico() {
        Agendamento agendamento = new Agendamento(
                10L,
                criarProfissional(),
                new Servico(1L, ServicoEnum.CORTE, Duration.ofMinutes(90)),
                criarEstabelecimento(),
                criarCliente(),
                LocalDateTime.of(2026, 4, 20, 14, 30),
                AgendamentoStatus.AGENDADO
        );

        assertEquals(LocalDateTime.of(2026, 4, 20, 16, 0), agendamento.getDataHoraFim());
        assertTrue(agendamento.toString().contains("Status: AGENDADO"));
    }

    @Test
    void agendamentoDeveRejeitarClienteNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Agendamento(1L, criarProfissional(), new Servico(ServicoEnum.CORTE, Duration.ofMinutes(60)), criarEstabelecimento(), null, LocalDateTime.now(), AgendamentoStatus.AGENDADO));

        assertEquals("Cliente não pode ser nulo", ex.getMessage());
    }

    @Test
    void avaliacaoDeveAceitarNotasValidasEComentarios() {
        Avaliacao avaliacao = new Avaliacao(
                30L,
                criarAgendamento(),
                4.5,
                5.0,
                "Ambiente limpo",
                "Profissional excelente"
        );

        assertEquals(30L, avaliacao.getId());
        assertEquals(4.5, avaliacao.getNotaEstabelecimento());
        assertEquals(5.0, avaliacao.getNotaProfissional());
        assertEquals("Ambiente limpo", avaliacao.getComentarioEstabelecimento());
    }

    @Test
    void avaliacaoDeveRejeitarNotaInvalida() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Avaliacao(1L, criarAgendamento(), 5.1, 4.0, null, null));

        assertEquals("Nota do estabelecimento deve ser entre 0 e 5", ex.getMessage());
    }

    @Test
    void avaliacaoDeveRejeitarComentarioMuitoGrande() {
        String comentarioGrande = "a".repeat(501);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Avaliacao(1L, criarAgendamento(), 4.0, 4.0, comentarioGrande, null));

        assertEquals("Comentário do estabelecimento não pode ter mais de 500 caracteres", ex.getMessage());
    }

    private static Agendamento criarAgendamento() {
        return new Agendamento(
                10L,
                criarProfissional(),
                new Servico(1L, ServicoEnum.CORTE, Duration.ofMinutes(60)),
                criarEstabelecimento(),
                criarCliente(),
                LocalDateTime.of(2026, 4, 20, 14, 30),
                AgendamentoStatus.AGENDADO
        );
    }

    private static Cliente criarCliente() {
        return new Cliente(1L, "Maria", new Cpf("52998224725"), new Celular("11987654321"), new Email("maria@teste.com"), Sexo.FEMININO);
    }

    private static Profissional criarProfissional() {
        return new Profissional(
                5L,
                "Ana Souza",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("ana@teste.com"),
                null,
                "Especialista em corte",
                List.of(new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), null, null)),
                Sexo.FEMININO,
                List.of()
        );
    }

    private static Estabelecimento criarEstabelecimento() {
        return new Estabelecimento(
                1L,
                "Studio Beleza",
                new Endereco("Rua A", "10", null, "Centro", "Sao Paulo", "SP", "01001000"),
                List.of(),
                List.of(),
                new Cnpj("11.444.777/0001-61"),
                List.of("foto.jpg"),
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );
    }
}