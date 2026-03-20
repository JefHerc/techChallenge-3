package com.fiap.gestao_servicos.core.domain;

import java.time.LocalDateTime;

public class Agendamento {

    private Long id;
    private Profissional profissional;
    private Servico servico;
    private Estabelecimento estabelecimento;
    private Cliente cliente;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private AgendamentoStatus status;

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

    public String toString() {
        return String.format(
                "Agendamento: Profissional: %s, Serviço: %s, Estabelecimento: %s, Cliente: %s, Data/Hora: %s, Status: %s",
                profissional != null ? profissional.getNome() : "N/A", servico != null ? servico.getNome() : "N/A",
                estabelecimento != null ? estabelecimento.getNome() : "N/A",
                cliente != null ? cliente.getNome() : "N/A", dataHoraInicio != null ? dataHoraInicio.toString() : "N/A",
                status != null ? status : "N/A");
    }
}
