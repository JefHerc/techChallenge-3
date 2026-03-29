package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
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

    @Column(nullable = false, unique = true)
    private String cnpj;

    @ElementCollection
    @CollectionTable(name = "estabelecimento_fotos", joinColumns = @JoinColumn(name = "estabelecimento_id"))
    @Column(name = "url_foto")
    private List<String> urlFotos;

    @ElementCollection
    @CollectionTable(name = "estabelecimento_horarios", joinColumns = @JoinColumn(name = "estabelecimento_id"))
    private List<HorarioFuncionamentoEmbeddable> horarioFuncionamento;

    @Transient
    private Double nota;

    public EstabelecimentoEntity() {}

    public EstabelecimentoEntity(String nome, EnderecoEntity endereco, String cnpj, List<String> urlFotos, List<HorarioFuncionamentoEmbeddable> horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.cnpj = cnpj;
        this.urlFotos = urlFotos;
        this.horarioFuncionamento = horarioFuncionamento;
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

    public EnderecoEntity getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoEntity endereco) {
        this.endereco = endereco;
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

    public List<HorarioFuncionamentoEmbeddable> getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(List<HorarioFuncionamentoEmbeddable> horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}


