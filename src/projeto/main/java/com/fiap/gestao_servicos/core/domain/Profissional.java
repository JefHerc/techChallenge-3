package com.fiap.gestao_servicos.core.domain;

import java.util.List;

public class Profissional {

    private Long id;
    private String nome;
    private Cpf cpf;
    private Celular celular;
    private Email email;
    private String urlFoto;
    private String descricao;
    private List<ExpedienteProfissional> expedientes;
    private Sexo sexo;
    private List<ServicoProfissional> servicosProfissional;

    public Profissional(Long id, String nome, Cpf cpf, Celular celular, Email email, String urlFoto, String descricao, List<ExpedienteProfissional> expedientes, Sexo sexo, List<ServicoProfissional> servicosProfissional) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.id = id;
        this.nome = nome.trim();
        this.expedientes = expedientes != null ? List.copyOf(expedientes) : List.of();

        this.cpf = cpf;

        this.celular = celular;

        this.email = email;

        this.urlFoto = urlFoto != null ? urlFoto.trim() : null;

        this.descricao = descricao != null ? descricao.trim() : null;
        this.sexo = sexo;
        this.servicosProfissional = servicosProfissional != null ? List.copyOf(servicosProfissional) : List.of();
    }

    public Profissional(String nome, Cpf cpf, Celular celular, Email email, String urlFoto, String descricao) {
        this(null, nome, cpf, celular, email, urlFoto, descricao, null, null, null);
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

    public Long getId() {
        return id;
    }

    public List<ExpedienteProfissional> getExpedientes() {
        return expedientes;
    }

    public List<ServicoProfissional> getServicosProfissional() {
        return servicosProfissional;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public String toString() {
        return String.format("Profissional: %s, CPF: %s, Celular: %s, Email: %s, URL Foto: %s, Descrição: %s",
                nome, cpf != null ? cpf.getMasked() : "N/A", celular != null ? celular.getMasked() : "N/A", email != null ? email.getNormalized() : "N/A", urlFoto != null ? urlFoto : "N/A", descricao != null ? descricao : "N/A");
    }
}

