package com.fiap.gestao_servicos.core.domain;

import java.time.LocalDateTime;

public class Agendamento {

    private final Long id;
    private final Profissional profissional;
    private final Servico servico;
    private final Estabelecimento estabelecimento;
    private final Cliente cliente;
    private final LocalDateTime dataHoraInicio;
    private final LocalDateTime dataHoraFim;
    private final AgendamentoStatus status;

    public Agendamento(Long id, Profissional profissional, Servico servico, Estabelecimento estabelecimento,
            Cliente cliente, LocalDateTime dataHoraInicio, AgendamentoStatus status) {
        if (profissional == null) {
            throw new IllegalArgumentException("Profissional não pode ser nulo");
        }
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não pode ser nulo");
        }
        if (estabelecimento == null) {
            throw new IllegalArgumentException("Estabelecimento não pode ser nulo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (dataHoraInicio == null) {
            throw new IllegalArgumentException("Data/Hora de início não pode ser nula");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }

        this.id = id;
        this.profissional = profissional;
        this.servico = servico;
        this.estabelecimento = estabelecimento;
        this.cliente = cliente;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraInicio.plusMinutes(servico.getDuracaoMedia().toMinutes());
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public Servico getServico() {
        return servico;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public AgendamentoStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(
                "Agendamento: Profissional: %s, Serviço: %s, Estabelecimento: %s, Cliente: %s, Data/Hora: %s, Status: %s",
                profissional.getNome(),
                servico.getNomeDescricao(),
                estabelecimento.getNome(),
                cliente.getNome(),
                dataHoraInicio.toString(),
                status);
    }
}


