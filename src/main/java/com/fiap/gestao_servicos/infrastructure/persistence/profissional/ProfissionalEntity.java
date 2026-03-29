package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profissional")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "Entidade JPA com coleções e associações gerenciadas pelo ORM")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id")
    private EstabelecimentoEntity estabelecimento;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ServicoProfissionalEntity> servicosProfissional = new ArrayList<>();

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ExpedienteProfissionalEntity> expedientes = new ArrayList<>();

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

    public List<ServicoProfissionalEntity> getServicosProfissional() {
        return servicosProfissional == null ? null : new ArrayList<>(servicosProfissional);
    }

    public void setServicosProfissional(List<ServicoProfissionalEntity> servicosProfissional) {
        this.servicosProfissional = servicosProfissional == null ? null : new ArrayList<>(servicosProfissional);
        if (this.servicosProfissional != null) {
            this.servicosProfissional.forEach(servicoProfissional -> servicoProfissional.setProfissional(this));
        }
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public EstabelecimentoEntity getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(EstabelecimentoEntity estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public List<ExpedienteProfissionalEntity> getExpedientes() {
        return expedientes == null ? null : new ArrayList<>(expedientes);
    }

    public void setExpedientes(List<ExpedienteProfissionalEntity> expedientes) {
        this.expedientes = expedientes == null ? null : new ArrayList<>(expedientes);
        if (this.expedientes != null) {
            this.expedientes.forEach(expediente -> expediente.setProfissional(this));
        }
    }
}

