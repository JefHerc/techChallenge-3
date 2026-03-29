package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = false)
public class EstabelecimentoDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @Valid
    private EnderecoDto endereco;
    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;
    private List<String> urlFotos;
    @Valid
    private List<HorarioFuncionamentoDto> horarioFuncionamento;

    public EstabelecimentoDto() {}

    public EstabelecimentoDto(String nome, EnderecoDto endereco, String cnpj, List<String> urlFotos, List<HorarioFuncionamentoDto> horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
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


