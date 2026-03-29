package com.fiap.gestao_servicos.infrastructure.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Endereco do estabelecimento")
public class EnderecoDto {

    @Schema(description = "Logradouro", example = "Rua das Flores", minLength = 2, maxLength = 120)
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(min = 2, max = 120, message = "Logradouro deve ter entre 2 e 120 caracteres")
    private String logradouro;
    @Schema(description = "Numero", example = "123", minLength = 1, maxLength = 20)
    @NotBlank(message = "Número é obrigatório")
    @Size(min = 1, max = 20, message = "Número deve ter entre 1 e 20 caracteres")
    private String numero;
    @Schema(description = "Complemento", example = "Sala 2", maxLength = 120)
    @Size(max = 120, message = "Complemento deve ter no máximo 120 caracteres")
    private String complemento;
    @Schema(description = "Bairro", example = "Centro", minLength = 2, maxLength = 80)
    @NotBlank(message = "Bairro é obrigatório")
    @Size(min = 2, max = 80, message = "Bairro deve ter entre 2 e 80 caracteres")
    private String bairro;
    @Schema(description = "Cidade", example = "Sao Paulo", minLength = 2, maxLength = 80)
    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 2, max = 80, message = "Cidade deve ter entre 2 e 80 caracteres")
    private String cidade;
    @Schema(description = "Estado (UF)", example = "SP", minLength = 2, maxLength = 2, pattern = "[A-Z]{2}")
    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser uma sigla de 2 letras maiúsculas")
    private String estado;
    @Schema(description = "CEP com ou sem hífen", example = "01001-000", minLength = 8, maxLength = 9, pattern = "\\d{5}-?\\d{3}")
    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 9, message = "CEP deve ter 8 dígitos (com ou sem hífen)")
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

