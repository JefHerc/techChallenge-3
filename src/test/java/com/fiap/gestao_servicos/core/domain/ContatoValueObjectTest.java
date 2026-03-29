package com.fiap.gestao_servicos.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContatoValueObjectTest {

    @Test
    void celularDeveAceitarValorValidoEMascarar() {
        Celular celular = new Celular("11987654321");

        assertEquals("11987654321", celular.getValue());
        assertEquals("(11) 98765-4321", celular.getMasked());
        assertEquals("(11) 98765-4321", celular.toString());
        assertTrue(celular.isValid());
    }

    @Test
    void celularDeveAceitarComparacaoPeloValorSemMascara() {
        Celular semMascara = new Celular("11987654321");
        Celular comMascara = new Celular("(11) 98765-4321");

        assertEquals(semMascara, comMascara);
        assertEquals(semMascara.hashCode(), comMascara.hashCode());
        assertTrue(comMascara.hasValidMask());
    }

    @Test
    void celularDeveRejeitarNumeroSemNonoDigito() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Celular("11887654321"));

        assertEquals("Celular inválido", ex.getMessage());
    }

    @Test
    void emailDeveNormalizarParaMinusculo() {
        Email email = new Email("Pessoa@Teste.COM");

        assertEquals("Pessoa@Teste.COM", email.getValue());
        assertEquals("pessoa@teste.com", email.getNormalized());
        assertEquals("pessoa@teste.com", email.toString());
    }

    @Test
    void emailDeveCompararIgnorandoCase() {
        Email primeiro = new Email("Pessoa@Teste.com");
        Email segundo = new Email("pessoa@teste.com");

        assertEquals(primeiro, segundo);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
    }

    @Test
    void emailDeveRejeitarFormatoInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Email("email-invalido"));

        assertEquals("Email inválido", ex.getMessage());
    }

    @Test
    void enderecoDeveNormalizarCamposEMascararCep() {
        Endereco endereco = new Endereco(" Rua das Flores ", " 123 ", " Sala 2 ", " Centro ", " Sao Paulo ", "SP", "01001-000");

        assertEquals("Rua das Flores", endereco.getLogradouro());
        assertEquals("123", endereco.getNumero());
        assertEquals("Sala 2", endereco.getComplemento());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("Sao Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("01001000", endereco.getCep());
        assertEquals("01001-000", endereco.getCepMasked());
        assertTrue(endereco.toString().contains("CEP: 01001-000"));
    }

    @Test
    void enderecoDeveRejeitarEstadoInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Endereco("Rua", "1", null, "Centro", "Sao Paulo", "sp", "01001000"));

        assertEquals("Estado deve ser uma sigla de 2 letras maiúsculas", ex.getMessage());
    }

    @Test
    void enderecoDeveRejeitarCepInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Endereco("Rua", "1", null, "Centro", "Sao Paulo", "SP", "123"));

        assertEquals("CEP deve conter 8 dígitos numéricos", ex.getMessage());
    }
}