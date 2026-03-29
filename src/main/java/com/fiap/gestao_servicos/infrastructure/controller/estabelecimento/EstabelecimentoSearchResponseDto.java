package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Dados resumidos de estabelecimento para resultado de busca")
public class EstabelecimentoSearchResponseDto {

    @Schema(description = "Identificador do estabelecimento", example = "1")
    private Long id;
    @Schema(description = "Nome do estabelecimento", example = "Studio Beleza Centro", minLength = 2, maxLength = 120)
    private String nome;
    @Schema(description = "Endereco do estabelecimento")
    private EnderecoDto endereco;
    @Schema(description = "Lista de URLs de fotos do estabelecimento")
    private List<String> urlFotos;
    @Schema(description = "Horario de funcionamento")
    private List<HorarioFuncionamentoDto> horarioFuncionamento;
    @Schema(description = "Nota media do estabelecimento", example = "4.7")
    private Double nota;
    @Schema(description = "Servicos ofertados pelo estabelecimento")
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

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public List<ServicoOfertadoDto> getServicosOfertados() {
        return servicosOfertados == null ? null : new ArrayList<>(servicosOfertados);
    }

    public void setServicosOfertados(List<ServicoOfertadoDto> servicosOfertados) {
        this.servicosOfertados = servicosOfertados == null ? null : new ArrayList<>(servicosOfertados);
    }
}


