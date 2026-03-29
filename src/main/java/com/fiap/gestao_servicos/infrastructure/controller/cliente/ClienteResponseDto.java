package com.fiap.gestao_servicos.infrastructure.controller.cliente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de cliente retornados pela API")
public class ClienteResponseDto {

    @Schema(description = "Identificador do cliente", example = "1")
    private Long id;
        @Schema(
            description = "Nome completo do cliente",
            example = "Maria da Silva",
            minLength = 2,
            maxLength = 120
        )
    private String nome;
    @Schema(description = "CPF do cliente", example = "12345678901")
    private String cpf;
    @Schema(description = "Telefone celular", example = "11987654321")
    private String celular;
    @Schema(description = "Email do cliente", example = "maria.silva@email.com")
    private String email;
        @Schema(
            description = "Sexo do cliente",
            example = "FEMININO",
            allowableValues = {"MASCULINO", "FEMININO", "OUTRO"}
        )
    private String sexo;

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}

