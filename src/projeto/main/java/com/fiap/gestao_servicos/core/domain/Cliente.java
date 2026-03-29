package com.fiap.gestao_servicos.core.domain;

public class Cliente {

    private final Long id;
    private final String nome;
    private final Cpf cpf;
    private final Celular celular;
    private final Email email;
    private final Sexo sexo;

    public Cliente(Long id, String nome, Cpf cpf, Celular celular, Email email, Sexo sexo) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.id = id;
        this.nome = nome.trim();

        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo");
        }
        this.cpf = cpf;

        if (celular == null) {
            throw new IllegalArgumentException("Celular não pode ser nulo");
        }
        this.celular = celular;

        if (email == null) {
            throw new IllegalArgumentException("Email não pode ser nulo");
        }
        this.email = email;

        if (sexo == null) {
            throw new IllegalArgumentException("Sexo não pode ser nulo");
        }
        this.sexo = sexo;
    }

    public Cliente(String nome, Cpf cpf, Celular celular, Email email, Sexo sexo) {
        this(null, nome, cpf, celular, email, sexo);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Celular getCelular() {
        return celular;
    }

    public Email getEmail() {
        return email;
    }

    public Sexo getSexo() {
        return sexo;
    }

    @Override
    public String toString() {
        return String.format("Cliente: %s, CPF: %s, Celular: %s, Email: %s, Sexo: %s",
                nome,
                cpf.getMasked(),
                celular.getMasked(),
                email.getNormalized(),
                sexo.name());
    }
}

