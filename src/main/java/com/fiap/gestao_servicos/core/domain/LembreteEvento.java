package com.fiap.gestao_servicos.core.domain;

import java.time.LocalDateTime;

public class LembreteEvento {

    private Long id;
    private Long agendamentoId;
    private LembreteTipo tipo;
    private LembreteDestinatario destinatario;
    private LembreteStatus status;
    private String mensagem;
    private String destinoEmail;
    private LocalDateTime criadoEm;
    private LocalDateTime enviadoEm;
    private String erro;

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

    public String getDestinoEmail() {
        return destinoEmail;
    }

    public void setDestinoEmail(String destinoEmail) {
        this.destinoEmail = destinoEmail;
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
