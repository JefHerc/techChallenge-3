package com.fiap.gestao_servicos.core.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EstabelecimentoFilter {

    private String nome;
    private String cidade;
    private String estado;
    private String bairro;
    private String servicoNome;
    private Double notaMinima;
    private Double profissionalNotaMinima;
    private BigDecimal precoMin;
    private BigDecimal precoMax;
    private LocalDate dataDisponivel;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getServicoNome() { return servicoNome; }
    public void setServicoNome(String servicoNome) { this.servicoNome = servicoNome; }
    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }
    public Double getProfissionalNotaMinima() { return profissionalNotaMinima; }
    public void setProfissionalNotaMinima(Double profissionalNotaMinima) { this.profissionalNotaMinima = profissionalNotaMinima; }
    public BigDecimal getPrecoMin() { return precoMin; }
    public void setPrecoMin(BigDecimal precoMin) { this.precoMin = precoMin; }
    public BigDecimal getPrecoMax() { return precoMax; }
    public void setPrecoMax(BigDecimal precoMax) { this.precoMax = precoMax; }
    public LocalDate getDataDisponivel() { return dataDisponivel; }
    public void setDataDisponivel(LocalDate dataDisponivel) { this.dataDisponivel = dataDisponivel; }
}
