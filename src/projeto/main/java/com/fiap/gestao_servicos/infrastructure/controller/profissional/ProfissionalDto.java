package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class ProfissionalDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @NotBlank(message = "Celular é obrigatório")
    private String celular;
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    private String urlFoto;
    private String descricao;
    private String sexo;
    @Valid
    private List<ExpedienteProfissionalDto> expedientes;
    @Valid
    private List<ServicoProfissionalDto> servicosProfissional;

    public ProfissionalDto() {}

    public ProfissionalDto(String nome, String cpf, String celular, String email, String urlFoto, String descricao) {
        this.nome = nome;
        this.cpf = cpf;
        this.celular = celular;
        this.email = email;
        this.urlFoto = urlFoto;
        this.descricao = descricao;
        this.sexo = null; // Initialize sexo
        this.expedientes = new ArrayList<>(); // Initialize expedientes
        this.servicosProfissional = new ArrayList<>(); // Initialize servicosProfissional
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

    public List<ServicoProfissionalDto> getServicosProfissional() {
        return servicosProfissional;
    }

    public void setServicosProfissional(List<ServicoProfissionalDto> servicosProfissional) {
        this.servicosProfissional = servicosProfissional;
    }
}


