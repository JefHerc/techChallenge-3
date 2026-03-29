package com.fiap.gestao_servicos.infrastructure.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EnderecoDto {

    @NotBlank(message = "Logradouro é obrigatório")
    private String logradouro;
    @NotBlank(message = "Número é obrigatório")
    private String numero;
    private String complemento;
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser uma sigla de 2 letras maiúsculas")
    private String estado;
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve conter 8 dígitos")
    private String cep;

    public EnderecoDto() {}

    public EnderecoDto(String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // Getters and Setters
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}

