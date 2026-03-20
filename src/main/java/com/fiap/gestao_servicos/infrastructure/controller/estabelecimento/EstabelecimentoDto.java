package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoDto;

import java.util.List;

public class EstabelecimentoDto {

    private String nome;
    private EnderecoDto endereco;
    private List<ProfissionalDto> profissionais;
    private List<ServicoDto> servicos;
    private String cnpj;
    private List<String> urlFotos;
    private String horarioFuncionamento;

    public EstabelecimentoDto() {}

    public EstabelecimentoDto(String nome, EnderecoDto endereco, List<ProfissionalDto> profissionais, List<ServicoDto> servicos, String cnpj, List<String> urlFotos, String horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.profissionais = profissionais;
        this.servicos = servicos;
        this.cnpj = cnpj;
        this.urlFotos = urlFotos;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EnderecoDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDto endereco) {
        this.endereco = endereco;
    }

    public List<ProfissionalDto> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(List<ProfissionalDto> profissionais) {
        this.profissionais = profissionais;
    }

    public List<ServicoDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoDto> servicos) {
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
