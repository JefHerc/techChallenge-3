package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

public class ProfissionalServicoDto {

    private Long id;
    private String nome;
    private String email;
    private String celular;
    private String urlFoto;
    private Double nota;

    public ProfissionalServicoDto() {}

    public ProfissionalServicoDto(Long id, String nome, String email, String celular, String urlFoto, Double nota) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.urlFoto = urlFoto;
        this.nota = nota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
