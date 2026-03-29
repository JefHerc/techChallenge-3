package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalSearchResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoResponseDto;

import java.util.List;

public class EstabelecimentoSearchResponseDto {

    private Long id;
    private String nome;
    private EnderecoDto endereco;
    private List<ProfissionalSearchResponseDto> profissionais;
    private List<ServicoResponseDto> servicos;
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

    public List<ProfissionalSearchResponseDto> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(List<ProfissionalSearchResponseDto> profissionais) {
        this.profissionais = profissionais;
    }

    public List<ServicoResponseDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoResponseDto> servicos) {
        this.servicos = servicos;
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


