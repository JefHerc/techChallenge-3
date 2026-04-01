package com.fiap.gestao_servicos.infrastructure.controller.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Duration;

@Schema(description = "Dados de servico retornados pela API")
public class ServicoResponseDto {

    @Schema(description = "Identificador do servico", example = "1")
    private Long id;

    @Schema(
            description = "Nome do servico",
            example = "CORTE",
            minLength = 2,
            maxLength = 120,
            allowableValues = {
                    "HIDRATACAO",
                    "CORTE",
                    "COLORACAO",
                    "PINTURA",
                    "ESCOVA",
                    "MANICURE",
                    "PEDICURE",
                    "MASSAGEM",
                    "DEPILACAO",
                    "BARBA",
                    "BIGODE",
                    "SOBRANCELHA",
                    "LIMPEZA_DE_PELE",
                    "ESCOVA_DEFINITIVA",
                    "PROGRESSIVA"
            }
    )
    private String nome;

    @Schema(
            description = "Duracao media em formato ISO-8601 duration (ex.: PT1H, PT45M, PT1H30M)",
            example = "PT1H",
            type = "string",
            format = "duration",
            pattern = "^P(?!$)(?:\\d+Y)?(?:\\d+M)?(?:\\d+W)?(?:\\d+D)?(?:T(?=\\d)(?:\\d+H)?(?:\\d+M)?(?:\\d+S)?)?$"
        )
    private Duration duracaoMedia;

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

    public Duration getDuracaoMedia() {
        return duracaoMedia;
    }

    public void setDuracaoMedia(Duration duracaoMedia) {
        this.duracaoMedia = duracaoMedia;
    }
}

