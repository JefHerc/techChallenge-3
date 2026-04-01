package com.fiap.gestao_servicos.core.domain;

import java.time.LocalDateTime;

public class LembreteEvento {

    private final Long id;
    private final Long agendamentoId;
    private final LembreteTipo tipo;
    private final LembreteDestinatario destinatario;
    private final LembreteStatus status;
    private final String mensagem;
    private final String destinoEmail;
    private final LocalDateTime criadoEm;
    private final LocalDateTime enviadoEm;
    private final String erro;

    public LembreteEvento(Long id, Long agendamentoId, LembreteTipo tipo,
                          LembreteDestinatario destinatario, LembreteStatus status,
                          String mensagem, String destinoEmail,
                          LocalDateTime criadoEm, LocalDateTime enviadoEm, String erro) {
        this.id = id;
        this.agendamentoId = agendamentoId;
        this.tipo = tipo;
        this.destinatario = destinatario;
        this.status = status;
        this.mensagem = mensagem;
        this.destinoEmail = destinoEmail;
        this.criadoEm = criadoEm;
        this.enviadoEm = enviadoEm;
        this.erro = erro;
    }

    public LembreteEvento comId(Long novoId) {
        return new LembreteEvento(novoId, agendamentoId, tipo, destinatario, status,
                mensagem, destinoEmail, criadoEm, enviadoEm, erro);
    }

    public LembreteEvento marcarComoEnviado(LocalDateTime momentoEnvio) {
        return new LembreteEvento(id, agendamentoId, tipo, destinatario, LembreteStatus.ENVIADO,
                mensagem, destinoEmail, criadoEm, momentoEnvio, null);
    }

    public LembreteEvento marcarComoFalha(String erroMensagem) {
        return new LembreteEvento(id, agendamentoId, tipo, destinatario, LembreteStatus.FALHA,
                mensagem, destinoEmail, criadoEm, enviadoEm, erroMensagem);
    }

    public Long getId() {
        return id;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public LembreteTipo getTipo() {
        return tipo;
    }

    public LembreteDestinatario getDestinatario() {
        return destinatario;
    }

    public LembreteStatus getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getDestinoEmail() {
        return destinoEmail;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getEnviadoEm() {
        return enviadoEm;
    }

    public String getErro() {
        return erro;
    }
}
