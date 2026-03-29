package com.fiap.gestao_servicos.core.exception;

public class NameAlreadyExistsException extends DuplicateDataException {

    public NameAlreadyExistsException(String nome) {
        super(nome + "já cadastrado");
    }
}

