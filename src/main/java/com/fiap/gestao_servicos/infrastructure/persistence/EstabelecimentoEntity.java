package com.fiap.gestao_servicos.infrastructure.persistence;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estabelecimento")
public class EstabelecimentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Embedded
    private EnderecoEntity endereco;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id")
    private List<ProfissionalEntity> profissionais;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id")
    private List<ServicoEntity> servicos;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @ElementCollection
    @CollectionTable(name = "estabelecimento_fotos", joinColumns = @JoinColumn(name = "estabelecimento_id"))
    @Column(name = "url_foto")
    private List<String> urlFotos;

    private String horarioFuncionamento;

    // Constructors
    public EstabelecimentoEntity() {}

    public EstabelecimentoEntity(String nome, EnderecoEntity endereco, List<ProfissionalEntity> profissionais, List<ServicoEntity> servicos, String cnpj, List<String> urlFotos, String horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.profissionais = profissionais;
        this.servicos = servicos;
        this.cnpj = cnpj;
        this.urlFotos = urlFotos;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    // Getters and Setters
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

    public EnderecoEntity getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoEntity endereco) {
        this.endereco = endereco;
    }

    public List<ProfissionalEntity> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(List<ProfissionalEntity> profissionais) {
        this.profissionais = profissionais;
    }

    public List<ServicoEntity> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoEntity> servicos) {
        this.servicos = servicos;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<String> getUrlFotos() {
        return urlFotos;
    }

    public void setUrlFotos(List<String> urlFotos) {
        this.urlFotos = urlFotos;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }
}
