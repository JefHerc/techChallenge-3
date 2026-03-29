package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Duration;

@Entity
@Table(name = "servico")
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Duration duracaoMedia;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private EstabelecimentoEntity estabelecimento;

    public ServicoEntity() {}

    public ServicoEntity(String nome, Duration duracaoMedia) {
        this.nome = nome;
        this.duracaoMedia = duracaoMedia;
    }

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

    public EstabelecimentoEntity getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(EstabelecimentoEntity estabelecimento) {
        this.estabelecimento = estabelecimento;
    }
}

