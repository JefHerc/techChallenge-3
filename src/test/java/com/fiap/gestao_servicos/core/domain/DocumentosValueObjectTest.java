package com.fiap.gestao_servicos.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentosValueObjectTest {

    @Test
    void cpfDeveAceitarValorValidoEMascararCorretamente() {
        Cpf cpf = new Cpf("52998224725");

        assertEquals("52998224725", cpf.getValue());
        assertEquals("529.982.247-25", cpf.getMasked());
        assertEquals("529.982.247-25", cpf.toString());
        assertTrue(cpf.isValid());
    }

    @Test
    void cpfDeveConsiderarIgualdadePeloValorNormalizado() {
        Cpf semMascara = new Cpf("52998224725");
        Cpf comMascara = new Cpf("529.982.247-25");

        assertEquals(semMascara, comMascara);
        assertEquals(semMascara.hashCode(), comMascara.hashCode());
    }

    @Test
    void cpfDeveRejeitarValorInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Cpf("11111111111"));

        assertEquals("CPF inválido", ex.getMessage());
    }

    @Test
    void cpfDeveRejeitarComprimentoInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Cpf("123"));

        assertEquals("CPF deve conter 11 dígitos", ex.getMessage());
    }

    @Test
    void cpfDeveInformarQuandoMascaraOriginalEhValida() {
        assertTrue(new Cpf("529.982.247-25").hasValidMask());
    }

    @Test
    void cnpjDeveAceitarValorValidoMascararENormalizar() {
        Cnpj cnpj = new Cnpj("11.444.777/0001-61");

        assertEquals("11.444.777/0001-61", cnpj.getValue());
        assertEquals("11444777000161", cnpj.getNormalizedValue());
        assertEquals("11.444.777/0001-61", cnpj.getMasked());
        assertEquals("11.444.777/0001-61", cnpj.toString());
        assertTrue(cnpj.isValid());
        assertTrue(cnpj.hasValidMask());
    }

    @Test
    void cnpjDeveConsiderarIgualdadePeloValorNormalizado() {
        Cnpj semMascara = new Cnpj("11444777000161");
        Cnpj comMascara = new Cnpj("11.444.777/0001-61");

        assertEquals(semMascara, comMascara);
        assertEquals(semMascara.hashCode(), comMascara.hashCode());
        assertNotEquals(semMascara, new Object());
    }

    @Test
    void cnpjDeveRejeitarValorInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Cnpj("11111111111111"));

        assertEquals("CNPJ inválido", ex.getMessage());
    }

    @Test
    void cnpjDeveRejeitarComprimentoInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Cnpj("123"));

        assertEquals("CNPJ deve conter 14 dígitos", ex.getMessage());
    }
}