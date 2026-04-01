package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Dados de profissional retornados pela API")
public class ProfissionalResponseDto {

    @Schema(description = "Identificador do profissional", example = "1")
    private Long id;
    @Schema(description = "Nome do profissional", example = "Carla Souza", minLength = 2, maxLength = 120)
    private String nome;
    @Schema(description = "CPF do profissional", example = "06854809096")
    private String cpf;
    @Schema(description = "Celular do profissional", example = "11988887777")
    private String celular;
    @Schema(description = "Email do profissional", example = "carla@e1.com")
    private String email;
    @Schema(description = "URL da foto", example = "https://cdn.exemplo.com/fotos/carla.jpg")
    private String urlFoto;
    @Schema(description = "Descricao resumida", example = "Especialista coloração", maxLength = 500)
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


