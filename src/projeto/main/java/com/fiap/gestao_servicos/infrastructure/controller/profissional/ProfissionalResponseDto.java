package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import java.util.List;

public class ProfissionalResponseDto {

    private Long id;
    private String nome;
    private String cpf;
    private String celular;
    private String email;
    private String urlFoto;
    private String descricao;
    private String sexo;
    private List<ExpedienteProfissionalDto> expedientes;
    private List<ServicoProfissionalResponseDto> servicosProfissional;

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<ExpedienteProfissionalDto> getExpedientes() {
        return expedientes;
    }

    public void setExpedientes(List<ExpedienteProfissionalDto> expedientes) {
        this.expedientes = expedientes;
    }

    public List<ServicoProfissionalResponseDto> getServicosProfissional() {
        return servicosProfissional;
    }

    public void setServicosProfissional(List<ServicoProfissionalResponseDto> servicosProfissional) {
        this.servicosProfissional = servicosProfissional;
    }
}


