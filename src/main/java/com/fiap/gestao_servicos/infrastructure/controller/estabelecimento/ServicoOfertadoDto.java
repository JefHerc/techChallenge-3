package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Servico ofertado no resultado de busca de estabelecimentos")
public class ServicoOfertadoDto {

    @Schema(description = "Identificador do servico", example = "20")
    private Long servicoId;
    @Schema(description = "Nome do servico", example = "Corte feminino", minLength = 2, maxLength = 120)
    private String servicoNome;
    @Schema(description = "Preco do servico", example = "120.00")
    private BigDecimal preco;
    @Schema(description = "Profissional associado ao servico")
    private ProfissionalServicoDto profissional;

    public ServicoOfertadoDto() {}

    public ServicoOfertadoDto(Long servicoId, String servicoNome, BigDecimal preco, ProfissionalServicoDto profissional) {
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.preco = preco;
        this.profissional = profissional;
    }

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public ProfissionalServicoDto getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalServicoDto profissional) {
        this.profissional = profissional;
    }
}
