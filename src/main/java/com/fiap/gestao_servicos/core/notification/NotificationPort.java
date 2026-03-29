package com.fiap.gestao_servicos.core.notification;

import com.fiap.gestao_servicos.core.domain.LembreteEvento;

public interface NotificationPort {

    void send(LembreteEvento evento);
}
