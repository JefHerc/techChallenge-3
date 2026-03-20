package com.fiap.gestao_servicos.core.domain;

import java.time.Duration;

public class Servico {

    private final Long id;
    private final String nome;
    private final Duration duracaoMedia;

    public Servico(Long id, String nome, Duration duracaoMedia) {
        this.id = id;
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();

        if (duracaoMedia == null) {
            throw new IllegalArgumentException("Duração média não pode ser nula");
        }
        if (duracaoMedia.isNegative() || duracaoMedia.isZero()) {
            throw new IllegalArgumentException("Duração média deve ser positiva");
        }
        this.duracaoMedia = duracaoMedia;
    }

    public Servico(String nome, Duration duracaoMedia) {
        this(null, nome, duracaoMedia);
    }

    public String getNome() {
        return nome;
    }

    public Duration getDuracaoMedia() {
        return duracaoMedia;
    }

    public Long getId() {
        return id;
    }
}
