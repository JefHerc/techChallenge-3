package com.fiap.gestao_servicos.core.domain;

public class ProfissionalServicoInfo {

    private final Long id;
    private final String nome;
    private final String email;
    private final String celular;
    private final String urlFoto;
    private final Double nota;

    public ProfissionalServicoInfo(Long id,
                                   String nome,
                                   String email,
                                   String celular,
                                   String urlFoto,
                                   Double nota) {
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

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public Double getNota() {
        return nota;
    }
}