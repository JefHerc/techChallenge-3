package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criacao/atualizacao de avaliacao")
public class AvaliacaoDto {

    @Schema(description = "ID do agendamento", example = "10")
    private Long agendamentoId;
    @Schema(description = "ID do profissional", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long profissionalId;
    @Schema(description = "ID do estabelecimento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long estabelecimentoId;
    @Schema(description = "Nota do estabelecimento de 0 a 5", example = "4.5")
    @DecimalMin(value = "0.0", message = "Nota do estabelecimento deve ser entre 0 e 5")
    @DecimalMax(value = "5.0", message = "Nota do estabelecimento deve ser entre 0 e 5")
    private double notaEstabelecimento;
    @Schema(description = "Nota do profissional de 0 a 5", example = "5.0")
    @DecimalMin(value = "0.0", message = "Nota do profissional deve ser entre 0 e 5")
    @DecimalMax(value = "5.0", message = "Nota do profissional deve ser entre 0 e 5")
    private double notaProfissional;
    @Schema(description = "Comentario sobre o estabelecimento", example = "Ambiente limpo e atendimento excelente")
    @Size(max = 500, message = "Comentário do estabelecimento não pode ter mais de 500 caracteres")
    private String comentarioEstabelecimento;
    @Schema(description = "Comentario sobre o profissional", example = "Profissional muito atenciosa")
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


