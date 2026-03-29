package com.fiap.gestao_servicos.core.domain;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PessoaEstabelecimentoDomainTest {

    @Test
    void clienteDeveExigirCamposObrigatoriosEFormatarToString() {
        Cliente cliente = new Cliente(
                1L,
                " Maria da Silva ",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("Maria@Teste.com"),
                Sexo.FEMININO
        );

        assertEquals("Maria da Silva", cliente.getNome());
        assertTrue(cliente.toString().contains("CPF: 529.982.247-25"));
        assertTrue(cliente.toString().contains("Email: maria@teste.com"));
    }

    @Test
    void clienteDeveRejeitarSexoNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Cliente("Maria", new Cpf("52998224725"), new Celular("11987654321"), new Email("maria@teste.com"), null));

        assertEquals("Sexo não pode ser nulo", ex.getMessage());
    }

    @Test
    void profissionalDeveCopiarListasTrimarCamposEOmitirNulosNoToString() {
        List<ExpedienteProfissional> expedientes = new ArrayList<>();
        expedientes.add(new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), null, null));

        Profissional profissional = new Profissional(
                2L,
                " Ana Souza ",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("Ana@Teste.com"),
                " https://cdn.exemplo.com/foto.jpg ",
                " Especialista em corte ",
                expedientes,
                Sexo.FEMININO,
                List.of()
        );
        expedientes.clear();

        assertEquals("Ana Souza", profissional.getNome());
        assertEquals("https://cdn.exemplo.com/foto.jpg", profissional.getUrlFoto());
        assertEquals("Especialista em corte", profissional.getDescricao());
        assertEquals(1, profissional.getExpedientes().size());
        assertTrue(profissional.toString().contains("Ana Souza"));
    }

    @Test
    void profissionalDeveRejeitarNomeVazio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Profissional("  ", null, null, null, null, null));

        assertEquals("Nome não pode ser nulo ou vazio", ex.getMessage());
    }

    @Test
    void estabelecimentoDeveCopiarColecoesEFormatarToString() {
        List<String> fotos = new ArrayList<>(List.of("foto-1.jpg"));
        List<HorarioFuncionamento> horarios = new ArrayList<>(List.of(
                new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false)
        ));

        Estabelecimento estabelecimento = new Estabelecimento(
                1L,
                " Studio Beleza ",
                new Endereco("Rua A", "10", null, "Centro", "Sao Paulo", "SP", "01001000"),
                List.of(),
                List.of(new Servico(ServicoEnum.CORTE, Duration.ofMinutes(60))),
                new Cnpj("11.444.777/0001-61"),
                fotos,
                horarios
        );
        fotos.clear();
        horarios.clear();

        assertEquals("Studio Beleza", estabelecimento.getNome());
        assertEquals(1, estabelecimento.getUrlFotos().size());
        assertEquals(1, estabelecimento.getHorarioFuncionamento().size());
        assertTrue(estabelecimento.toString().contains("Studio Beleza"));
    }

    @Test
    void estabelecimentoDeveRejeitarNomeVazio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Estabelecimento(null, " ", null, null, null, null, null, null));

        assertEquals("Nome não pode ser nulo ou vazio", ex.getMessage());
    }
}