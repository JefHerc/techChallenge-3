package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de avaliacao retornados pela API")
public class AvaliacaoResponseDto {

    @Schema(description = "Identificador da avaliacao", example = "30")
    private Long id;
    @Schema(description = "ID do agendamento", example = "10")
    private Long agendamentoId;
    @Schema(description = "ID do profissional", example = "5")
    private Long profissionalId;
    @Schema(description = "Nome do profissional", example = "Ana Souza", minLength = 2, maxLength = 120)
    private String profissionalNome;
    @Schema(description = "ID do estabelecimento", example = "1")
    private Long estabelecimentoId;
    @Schema(description = "Nome do estabelecimento", example = "Studio Beleza Centro", minLength = 2, maxLength = 120)
    private String estabelecimentoNome;
        @Schema(
            description = "Status do agendamento",
            example = "CONCLUIDO",
            allowableValues = {"AGENDADO", "CANCELADO", "CONCLUIDO"}
        )
    private String statusAgendamento;
    @Schema(description = "Nota do estabelecimento", example = "4.5")
    private double notaEstabelecimento;
    @Schema(description = "Nota do profissional", example = "5.0")
    private double notaProfissional;
    @Schema(description = "Comentario sobre o estabelecimento", example = "Ambiente limpo e atendimento excelente", maxLength = 500)
    private String comentarioEstabelecimento;
    @Schema(description = "Comentario sobre o profissional", example = "Profissional muito atenciosa", maxLength = 500)
    private String comentarioProfissional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public String getProfissionalNome() {
        return profissionalNome;
    }

    public void setProfissionalNome(String profissionalNome) {
        this.profissionalNome = profissionalNome;
    }

    public Long getEstabelecimentoId() {
        return estabelecimentoId;
    }

    public void setEstabelecimentoId(Long estabelecimentoId) {
        this.estabelecimentoId = estabelecimentoId;
    }

    public String getEstabelecimentoNome() {
        return estabelecimentoNome;
    }

    public void setEstabelecimentoNome(String estabelecimentoNome) {
        this.estabelecimentoNome = estabelecimentoNome;
    }

    public String getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(String statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public double getNotaEstabelecimento() {
        return notaEstabelecimento;
    }

    public void setNotaEstabelecimento(double notaEstabelecimento) {
        this.notaEstabelecimento = notaEstabelecimento;
    }

    public double getNotaProfissional() {
        return notaProfissional;
    }

    public void setNotaProfissional(double notaProfissional) {
        this.notaProfissional = notaProfissional;
    }

    public String getComentarioEstabelecimento() {
        return comentarioEstabelecimento;
    }

    public void setComentarioEstabelecimento(String comentarioEstabelecimento) {
        this.comentarioEstabelecimento = comentarioEstabelecimento;
    }

    public String getComentarioProfissional() {
        return comentarioProfissional;
    }

    public void setComentarioProfissional(String comentarioProfissional) {
        this.comentarioProfissional = comentarioProfissional;
    }
}


