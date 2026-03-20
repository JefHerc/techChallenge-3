package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoResponseDto;

import java.util.List;

public class EstabelecimentoResponseDto {

    private Long id;
    private String nome;
    private EnderecoDto endereco;
    private List<ProfissionalResponseDto> profissionais;
    private List<ServicoResponseDto> servicos;
    private String cnpj;
    private List<String> urlFotos;
    private String horarioFuncionamento;

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

    public EnderecoDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDto endereco) {
        this.endereco = endereco;
    }

    public List<ProfissionalResponseDto> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(List<ProfissionalResponseDto> profissionais) {
        this.profissionais = profissionais;
    }

    public List<ServicoResponseDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoResponseDto> servicos) {
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