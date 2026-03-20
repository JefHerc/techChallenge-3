package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

public class AvaliacaoDto {

    private Long agendamentoId;
    private Long profissionalId;
    private Long estabelecimentoId;
    private double notaEstabelecimento;
    private double notaProfissional;
    private String comentarioEstabelecimento;
    private String comentarioProfissional;

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

    public Long getEstabelecimentoId() {
        return estabelecimentoId;
    }

    public void setEstabelecimentoId(Long estabelecimentoId) {
        this.estabelecimentoId = estabelecimentoId;
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
