package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Dados de profissional retornados pela API")
public class ProfissionalResponseDto {

    @Schema(description = "Identificador do profissional", example = "5")
    private Long id;
    @Schema(description = "Nome do profissional", example = "Ana Souza", minLength = 2, maxLength = 120)
    private String nome;
    @Schema(description = "CPF do profissional", example = "12345678901")
    private String cpf;
    @Schema(description = "Celular do profissional", example = "11987654321")
    private String celular;
    @Schema(description = "Email do profissional", example = "ana.souza@email.com")
    private String email;
    @Schema(description = "URL da foto", example = "https://cdn.exemplo.com/fotos/ana.jpg")
    private String urlFoto;
    @Schema(description = "Descricao resumida", example = "Especialista em coloracao e corte", maxLength = 500)
    private String descricao;
        @Schema(
            description = "Sexo do profissional",
            example = "FEMININO",
            allowableValues = {"MASCULINO", "FEMININO", "OUTRO"}
        )
    private String sexo;
    @Schema(description = "Expedientes de atendimento")
    private List<ExpedienteProfissionalDto> expedientes;
    @Schema(description = "Servicos ofertados pelo profissional")
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
        return expedientes == null ? null : new ArrayList<>(expedientes);
    }

    public void setExpedientes(List<ExpedienteProfissionalDto> expedientes) {
        this.expedientes = expedientes == null ? null : new ArrayList<>(expedientes);
    }

    public List<ServicoProfissionalResponseDto> getServicosProfissional() {
        return servicosProfissional == null ? null : new ArrayList<>(servicosProfissional);
    }

    public void setServicosProfissional(List<ServicoProfissionalResponseDto> servicosProfissional) {
        this.servicosProfissional = servicosProfissional == null ? null : new ArrayList<>(servicosProfissional);
    }
}


