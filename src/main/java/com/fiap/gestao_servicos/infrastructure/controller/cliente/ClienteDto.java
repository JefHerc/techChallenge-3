package com.fiap.gestao_servicos.infrastructure.controller.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criacao/atualizacao de cliente")
public class ClienteDto {

        @Schema(
            description = "Nome completo do cliente",
            example = "Maria da Silva",
            minLength = 2,
            maxLength = 120
        )
    @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 120, message = "Nome deve ter entre 2 e 120 caracteres")
    private String nome;
    @Schema(description = "CPF do cliente (somente numeros)", example = "12345678901")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @Schema(description = "Telefone celular com DDD", example = "11987654321")
    @NotBlank(message = "Celular é obrigatório")
    private String celular;
    @Schema(description = "Email do cliente", example = "maria.silva@email.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
        @Schema(
            description = "Sexo do cliente",
            example = "FEMININO",
            allowableValues = {"MASCULINO", "FEMININO", "OUTRO"}
        )
    @NotBlank(message = "Sexo é obrigatório")
    private String sexo;

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

