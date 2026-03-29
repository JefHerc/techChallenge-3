package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import java.time.LocalDateTime;

public class AgendamentoResponseDto {

    private Long id;
    private Long profissionalId;
    private String profissionalNome;
    private Long servicoId;
    private String servicoNome;
    private Long estabelecimentoId;
    private String estabelecimentoNome;
    private Long clienteId;
    private String clienteNome;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

