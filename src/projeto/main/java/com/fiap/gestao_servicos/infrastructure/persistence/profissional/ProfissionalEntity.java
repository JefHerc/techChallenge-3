package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "profissional")
public class ProfissionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String celular;

    @Column(nullable = false, unique = true)
    private String email;

    private String urlFoto;

    private String descricao;

    private String sexo;

    @OneToOne(mappedBy = "profissional", fetch = FetchType.LAZY)
    private ServicoProfissionalEntity servicoProfissional;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id")
    private java.util.List<ExpedienteProfissionalEntity> expedientes;

    public ProfissionalEntity() {}

    public ProfissionalEntity(String nome, String cpf, String celular, String email, String urlFoto, String descricao) {
        this.nome = nome;
        this.cpf = cpf;
        this.celular = celular;
        this.email = email;
        this.urlFoto = urlFoto;
        this.descricao = descricao;
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

    public ServicoProfissionalEntity getServicoProfissional() {
        return servicoProfissional;
    }

    public void setServicoProfissional(ServicoProfissionalEntity servicoProfissional) {
        this.servicoProfissional = servicoProfissional;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public java.util.List<ExpedienteProfissionalEntity> getExpedientes() {
        return expedientes;
    }

    public void setExpedientes(java.util.List<ExpedienteProfissionalEntity> expedientes) {
        this.expedientes = expedientes;
    }
}

