package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = false)
@Schema(description = "Payload para criacao/atualizacao de estabelecimento")
public class EstabelecimentoDto {

        @Schema(
            description = "Nome do estabelecimento",
            example = "Studio Bela Vida",
            minLength = 2,
            maxLength = 120
        )
    @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 120, message = "Nome deve ter entre 2 e 120 caracteres")
    private String nome;
    @Schema(description = "Endereco do estabelecimento")
    @Valid
    private EnderecoDto endereco;
    @Schema(description = "CNPJ do estabelecimento (somente numeros)", example = "64245654000168")
    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;
    @Schema(description = "Lista de URLs de fotos do estabelecimento")
    private List<String> urlFotos;
    @Schema(description = "Horario de funcionamento do estabelecimento")
    @Valid
    private List<HorarioFuncionamentoDto> horarioFuncionamento;

    public EstabelecimentoDto() {}

    public EstabelecimentoDto(String nome, EnderecoDto endereco, String cnpj, List<String> urlFotos, List<HorarioFuncionamentoDto> horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.cnpj = cnpj;
        this.urlFotos = urlFotos == null ? null : new ArrayList<>(urlFotos);
        this.horarioFuncionamento = horarioFuncionamento == null ? null : new ArrayList<>(horarioFuncionamento);
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


