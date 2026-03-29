package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados resumidos de profissional para listagens e busca")
public class ProfissionalSearchResponseDto {

    @Schema(description = "Identificador do profissional", example = "5")
    private Long id;
    @Schema(description = "Nome do profissional", example = "Ana Souza", minLength = 2, maxLength = 120)
    private String nome;
    @Schema(description = "URL da foto do profissional", example = "https://cdn.exemplo.com/fotos/ana.jpg")
    private String urlFoto;
    @Schema(description = "Descricao resumida do profissional", example = "Especialista em coloracao e corte", maxLength = 500)
    private String descricao;

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
}


