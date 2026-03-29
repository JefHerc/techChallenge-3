package com.fiap.gestao_servicos.core.domain;

public enum ServicoEnum {

    HIDRATACAO("Hidratação"),
    CORTE("Corte"),
    PINTURA("Pintura"),
    MANICURE("Manicure"),
    PEDICURE("Pedicure"),
    MASSAGEM("Massagem"),
    DEPILACAO("Depilação"),
    BARBA("Barba"),
    BIGODE("Bigode"),
    SOBRANCELHA("Sobrancelha"),
    LIMPEZA_DE_PELE("Limpeza de Pele"),
    ESCOVA_DEFINITIVA("Escova Definitiva"),
    PROGRESSIVA("Progressiva");

    private final String descricao;

    ServicoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


