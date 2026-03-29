package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;

import java.util.List;

public class EstabelecimentoSearchResponseDto {

    private Long id;
    private String nome;
    private EnderecoDto endereco;
    private List<String> urlFotos;
    private List<HorarioFuncionamentoDto> horarioFuncionamento;
    private Double nota;
    private List<ServicoOfertadoDto> servicosOfertados;

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

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public List<ServicoOfertadoDto> getServicosOfertados() {
        return servicosOfertados;
    }

    public void setServicosOfertados(List<ServicoOfertadoDto> servicosOfertados) {
        this.servicosOfertados = servicosOfertados;
    }
}


