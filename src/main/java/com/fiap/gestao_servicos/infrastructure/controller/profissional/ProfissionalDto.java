package com.fiap.gestao_servicos.infrastructure.controller.profissional;

public class ProfissionalDto {

    private String nome;
    private String cpf;
    private String celular;
    private String email;
    private String urlFoto;
    private String descricao;

    public ProfissionalDto() {}

    public ProfissionalDto(String nome, String cpf, String celular, String email, String urlFoto, String descricao) {
        this.nome = nome;
        this.cpf = cpf;
        this.celular = celular;
        this.email = email;
        this.urlFoto = urlFoto;
        this.descricao = descricao;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}