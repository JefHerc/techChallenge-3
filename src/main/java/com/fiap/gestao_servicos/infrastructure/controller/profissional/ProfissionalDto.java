package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Payload para criacao/atualizacao de profissional")
public class ProfissionalDto {

        @Schema(
            description = "Nome do profissional",
            example = "Ana Souza",
            minLength = 2,
            maxLength = 120
        )
    @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 120, message = "Nome deve ter entre 2 e 120 caracteres")
    private String nome;
    @Schema(description = "CPF do profissional", example = "12345678901")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @Schema(description = "Celular do profissional", example = "11987654321")
    @NotBlank(message = "Celular é obrigatório")
    private String celular;
    @Schema(description = "Email do profissional", example = "ana.souza@email.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @Schema(description = "URL da foto do profissional", example = "https://cdn.exemplo.com/fotos/ana.jpg")
    private String urlFoto;
        @Schema(
            description = "Descricao resumida do profissional",
            example = "Especialista em coloracao e corte",
            maxLength = 500
        )
        @Size(max = 500, message = "Descricao deve ter no máximo 500 caracteres")
    private String descricao;
        @Schema(
            description = "Sexo do profissional",
            example = "FEMININO",
            allowableValues = {"MASCULINO", "FEMININO", "OUTRO"}
        )
    private String sexo;
    @Schema(description = "Lista de expedientes do profissional")
    @Valid
    private List<ExpedienteProfissionalDto> expedientes;
    @Schema(description = "Lista de servicos e precos do profissional")
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
        this.sexo = null;
        this.expedientes = new ArrayList<>();
        this.servicosProfissional = new ArrayList<>();
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
        return expedientes == null ? null : new ArrayList<>(expedientes);
    }

    public void setExpedientes(List<ExpedienteProfissionalDto> expedientes) {
        this.expedientes = expedientes == null ? null : new ArrayList<>(expedientes);
    }

    public List<ServicoProfissionalDto> getServicosProfissional() {
        return servicosProfissional == null ? null : new ArrayList<>(servicosProfissional);
    }

    public void setServicosProfissional(List<ServicoProfissionalDto> servicosProfissional) {
        this.servicosProfissional = servicosProfissional == null ? null : new ArrayList<>(servicosProfissional);
    }
}


