package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estabelecimento")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "Entidade JPA com estado gerenciado pelo ORM")
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
        this.urlFotos = urlFotos == null ? null : new ArrayList<>(urlFotos);
        this.horarioFuncionamento = horarioFuncionamento == null ? null : new ArrayList<>(horarioFuncionamento);
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
        return urlFotos == null ? null : new ArrayList<>(urlFotos);
    }

    public void setUrlFotos(List<String> urlFotos) {
        this.urlFotos = urlFotos == null ? null : new ArrayList<>(urlFotos);
    }

    public List<HorarioFuncionamentoEmbeddable> getHorarioFuncionamento() {
        return horarioFuncionamento == null ? null : new ArrayList<>(horarioFuncionamento);
    }

    public void setHorarioFuncionamento(List<HorarioFuncionamentoEmbeddable> horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento == null ? null : new ArrayList<>(horarioFuncionamento);
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}


