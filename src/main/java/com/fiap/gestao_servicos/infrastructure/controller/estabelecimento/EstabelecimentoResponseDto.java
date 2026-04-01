package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Dados de estabelecimento retornados pela API")
public class EstabelecimentoResponseDto {

    @Schema(description = "Identificador do estabelecimento", example = "1")
    private Long id;
    @Schema(description = "Nome do estabelecimento", example = "Studio Bela Vida", minLength = 2, maxLength = 120)
    private String nome;
    @Schema(description = "Endereco do estabelecimento")
    private EnderecoDto endereco;
    @Schema(description = "CNPJ do estabelecimento", example = "64245654000168")
    private String cnpj;
    @Schema(description = "Lista de URLs de fotos do estabelecimento")
    private List<String> urlFotos;
    @Schema(description = "Horario de funcionamento")
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
        return urlFotos == null ? null : new ArrayList<>(urlFotos);
    }

    public void setUrlFotos(List<String> urlFotos) {
        this.urlFotos = urlFotos == null ? null : new ArrayList<>(urlFotos);
    }

    public List<HorarioFuncionamentoDto> getHorarioFuncionamento() {
        return horarioFuncionamento == null ? null : new ArrayList<>(horarioFuncionamento);
    }

    public void setHorarioFuncionamento(List<HorarioFuncionamentoDto> horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento == null ? null : new ArrayList<>(horarioFuncionamento);
    }
}


