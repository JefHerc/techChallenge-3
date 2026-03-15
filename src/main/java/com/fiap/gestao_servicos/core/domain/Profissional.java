package com.fiap.gestao_servicos.core.domain;

public class Profissional {

    private Long id;
    private String nome;
    private Cpf cpf;
    private Celular celular;
    private Email email;
    private String urlFoto;
    private String descricao;

    public Profissional(String nome, Cpf cpf, Celular celular, Email email, String urlFoto, String descricao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();

        this.cpf = cpf;

        this.celular = celular;

        this.email = email;

        this.urlFoto = urlFoto != null ? urlFoto.trim() : null;

        this.descricao = descricao != null ? descricao.trim() : null;
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

    public String getUrlFoto() {
        return urlFoto;
    }

    public String getDescricao() {
        return descricao;
    }

    public String toString() {
        return String.format("Profissional: %s, CPF: %s, Celular: %s, Email: %s, URL Foto: %s, Descrição: %s",
                nome, cpf != null ? cpf.getMasked() : "N/A", celular != null ? celular.getMasked() : "N/A", email != null ? email.getNormalized() : "N/A", urlFoto != null ? urlFoto : "N/A", descricao != null ? descricao : "N/A");
    }
}