package com.fiap.gestao_servicos.core.domain;

import java.time.Duration;

public class Servico {

    private final Long id;
    private final ServicoEnum nome;
    private final Duration duracaoMedia;

    public Servico(Long id, ServicoEnum nome, Duration duracaoMedia) {
        this.id = id;
        if (nome == null) {
            throw new IllegalArgumentException("Nome não pode ser nulo");
        }
        this.nome = nome;

        if (duracaoMedia == null) {
            throw new IllegalArgumentException("Duração média não pode ser nula");
        }
        if (duracaoMedia.isNegative() || duracaoMedia.isZero()) {
            throw new IllegalArgumentException("Duração média deve ser positiva");
        }
        this.duracaoMedia = duracaoMedia;
    }

    public Servico(ServicoEnum nome, Duration duracaoMedia) {
        this(null, nome, duracaoMedia);
    }

    public ServicoEnum getNome() {
        return nome;
    }

    public String getNomeAsString() {
        return nome != null ? nome.name() : null;
    }

    public String getNomeDescricao() {
        return nome != null ? nome.getDescricao() : null;
    }

    public Duration getDuracaoMedia() {
        return duracaoMedia;
    }

    public Long getId() {
        return id;
    }
}


