package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public class AvaliacaoDto {

    private Long agendamentoId;
    private Long profissionalId;
    private Long estabelecimentoId;
    @DecimalMin(value = "0.0", message = "Nota do estabelecimento deve ser entre 0 e 5")
    @DecimalMax(value = "5.0", message = "Nota do estabelecimento deve ser entre 0 e 5")
    private double notaEstabelecimento;
    @DecimalMin(value = "0.0", message = "Nota do profissional deve ser entre 0 e 5")
    @DecimalMax(value = "5.0", message = "Nota do profissional deve ser entre 0 e 5")
    private double notaProfissional;
    @Size(max = 500, message = "Comentário do estabelecimento não pode ter mais de 500 caracteres")
    private String comentarioEstabelecimento;
    @Size(max = 500, message = "Comentário do profissional não pode ter mais de 500 caracteres")
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


