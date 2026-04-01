package com.fiap.gestao_servicos.infrastructure.controller.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Duration;

@Schema(description = "Payload para criacao/atualizacao de servico")
public class ServicoDto {

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
    @NotBlank(message = "Nome do serviço é obrigatório")
        @Size(min = 2, max = 120, message = "Nome do serviço deve ter entre 2 e 120 caracteres")
    private String nome;
        @Schema(
            description = "Duracao media do servico em formato ISO-8601 duration (ex.: PT1H, PT45M, PT1H30M)",
            example = "PT1H",
            type = "string",
            format = "duration",
            pattern = "^P(?!$)(?:\\d+Y)?(?:\\d+M)?(?:\\d+W)?(?:\\d+D)?(?:T(?=\\d)(?:\\d+H)?(?:\\d+M)?(?:\\d+S)?)?$"
        )
    @NotNull(message = "Duração média é obrigatória")
    private Duration duracaoMedia;

    public ServicoDto() {}

    public ServicoDto(String nome, Duration duracaoMedia) {
        this.nome = nome;
        this.duracaoMedia = duracaoMedia;
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

