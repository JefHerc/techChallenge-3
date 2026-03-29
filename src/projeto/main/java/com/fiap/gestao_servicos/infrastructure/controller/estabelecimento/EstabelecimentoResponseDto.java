package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;

import java.util.List;

public class EstabelecimentoResponseDto {

    private Long id;
    private String nome;
    private EnderecoDto endereco;
    private String cnpj;
    private List<String> urlFotos;
    private List<HorarioFuncionamentoDto> horarioFuncionamento;

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

    public List<HorarioFuncionamentoDto> getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(List<HorarioFuncionamentoDto> horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }
}


