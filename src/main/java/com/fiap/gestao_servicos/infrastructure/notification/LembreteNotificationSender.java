package com.fiap.gestao_servicos.infrastructure.notification;

import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LembreteNotificationSender implements NotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(LembreteNotificationSender.class);
    private final JavaMailSender mailSender;
    private final String from;

    public LembreteNotificationSender(JavaMailSender mailSender,
                                      @Value("${notification.email.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(LembreteEvento evento) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(evento.getDestinoEmail());
        message.setSubject(montarAssunto(evento));
        message.setText(evento.getMensagem());

        mailSender.send(message);

        LOGGER.info("[LEMBRETE] tipo={} destinatario={} destino={} agendamentoId={} mensagem={}",
                evento.getTipo(),
                evento.getDestinatario(),
                evento.getDestinoEmail(),
                evento.getAgendamentoId(),
                evento.getMensagem());
    }

    private String montarAssunto(LembreteEvento evento) {
        return "Lembrete de atendimento - " + evento.getTipo();
    }
}


