package com.fiap.gestao_servicos.infrastructure.persistence.notificacao;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "lembrete_evento",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_lembrete_unico", columnNames = {"agendamento_id", "tipo", "destinatario"})
        }
)
public class LembreteEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private AgendamentoEntity agendamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LembreteTipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LembreteDestinatario destinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LembreteStatus status;

    @Column(nullable = false, length = 400)
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime enviadoEm;

    @Column(length = 500)
    private String erro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgendamentoEntity getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoEntity agendamento) {
        this.agendamento = agendamento;
    }

    public LembreteTipo getTipo() {
        return tipo;
    }

    public void setTipo(LembreteTipo tipo) {
        this.tipo = tipo;
    }

    public LembreteDestinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(LembreteDestinatario destinatario) {
        this.destinatario = destinatario;
    }

    public LembreteStatus getStatus() {
        return status;
    }

    public void setStatus(LembreteStatus status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getEnviadoEm() {
        return enviadoEm;
    }

    public void setEnviadoEm(LocalDateTime enviadoEm) {
        this.enviadoEm = enviadoEm;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}


