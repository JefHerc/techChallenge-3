package com.fiap.gestao_servicos.infrastructure.controller.servico;

import java.time.Duration;

public class ServicoDto {

    private String nome;
    private Duration duracaoMedia;

    public ServicoDto() {}

    public ServicoDto(String nome, Duration duracaoMedia) {
        this.nome = nome;
        this.duracaoMedia = duracaoMedia;
    }

    // Getters and Setters
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