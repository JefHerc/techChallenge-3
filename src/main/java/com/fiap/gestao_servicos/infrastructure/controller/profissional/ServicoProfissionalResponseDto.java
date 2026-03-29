package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Servico ofertado por um profissional")
public class ServicoProfissionalResponseDto {

    @Schema(description = "Identificador do servico", example = "20")
    private Long servicoId;
    @Schema(description = "Nome do servico", example = "Corte feminino", minLength = 2, maxLength = 120)
    private String servicoNome;
    @Schema(description = "Valor cobrado pelo servico", example = "120.00")
    private BigDecimal valor;

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}


