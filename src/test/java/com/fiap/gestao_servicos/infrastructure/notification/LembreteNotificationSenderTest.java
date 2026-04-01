package com.fiap.gestao_servicos.infrastructure.notification;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LembreteNotificationSenderTest {

    @Test
    void deveMontarAssuntoECorpoComDadosDoAgendamento() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);

        LembreteNotificationSender sender = new LembreteNotificationSender(
                mailSender,
                agendamentoRepository,
                "no-reply@gestao-servicos.local");

        Agendamento agendamento = criarAgendamento();
        when(agendamentoRepository.findById(48L)).thenReturn(Optional.of(agendamento));

        LembreteEvento evento = new LembreteEvento(
                null, 48L, LembreteTipo.T_24_HORAS, LembreteDestinatario.CLIENTE,
                LembreteStatus.PENDENTE, "fallback", "joao@cliente.com", null, null, null);

        sender.send(evento);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("LEMBRETE CLIENTE (24H): Studio Bela Vida - Corte - 01/04/2026", message.getSubject());
        assertTrue(message.getText().contains("Olá, João!"));
        assertTrue(message.getText().contains("- Nome: Studio Bela Vida"));
        assertTrue(message.getText().contains("- Nome: Bruno Costa"));
        assertTrue(message.getText().contains("- Serviço: Corte"));
        assertTrue(message.getText().contains("- Início: 01/04/2026 17:30"));
        assertTrue(message.getText().contains("- Fim: 01/04/2026 18:30"));
    }

        @Test
        void deveMontarCorpoEspecificoParaProfissional() {
                JavaMailSender mailSender = mock(JavaMailSender.class);
                AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);

                LembreteNotificationSender sender = new LembreteNotificationSender(
                                mailSender,
                                agendamentoRepository,
                                "no-reply@gestao-servicos.local");

                Agendamento agendamento = criarAgendamento();
                when(agendamentoRepository.findById(48L)).thenReturn(Optional.of(agendamento));

                LembreteEvento evento = new LembreteEvento(
                        null, 48L, LembreteTipo.T_24_HORAS, LembreteDestinatario.PROFISSIONAL,
                        LembreteStatus.PENDENTE, "fallback", "bruno@e1.com", null, null, null);

                sender.send(evento);

                ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
                verify(mailSender).send(captor.capture());

                SimpleMailMessage message = captor.getValue();
                assertEquals("LEMBRETE PROFISSIONAL (24H): Studio Bela Vida - Corte - 01/04/2026", message.getSubject());
                assertTrue(message.getText().contains("Olá, Bruno!"));
                assertTrue(message.getText().contains("Você tem um atendimento agendado."));
                assertTrue(message.getText().contains("Ótimo atendimento!"));
        }

        @Test
        void deveMontarEmailDeCancelamentoParaCliente() {
                JavaMailSender mailSender = mock(JavaMailSender.class);
                AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);

                LembreteNotificationSender sender = new LembreteNotificationSender(
                                mailSender,
                                agendamentoRepository,
                                "no-reply@gestao-servicos.local");

                Agendamento agendamento = criarAgendamentoCancelado();
                when(agendamentoRepository.findById(48L)).thenReturn(Optional.of(agendamento));

                LembreteEvento evento = new LembreteEvento(
                        null, 48L, null, LembreteDestinatario.CLIENTE,
                        null, "fallback", "joao@cliente.com", null, null, null);

                sender.send(evento);

                ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
                verify(mailSender).send(captor.capture());

                SimpleMailMessage message = captor.getValue();
                assertEquals("CANCELAMENTO CLIENTE: Studio Bela Vida - Corte - 01/04/2026", message.getSubject());
                assertTrue(message.getText().contains("Seu agendamento foi cancelado."));
        }

        @Test
        void deveMontarEmailDeCancelamentoParaProfissional() {
                JavaMailSender mailSender = mock(JavaMailSender.class);
                AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);

                LembreteNotificationSender sender = new LembreteNotificationSender(
                                mailSender,
                                agendamentoRepository,
                                "no-reply@gestao-servicos.local");

                Agendamento agendamento = criarAgendamentoCancelado();
                when(agendamentoRepository.findById(48L)).thenReturn(Optional.of(agendamento));

                LembreteEvento evento = new LembreteEvento(
                        null, 48L, null, LembreteDestinatario.PROFISSIONAL,
                        null, "fallback", "bruno@e1.com", null, null, null);

                sender.send(evento);

                ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
                verify(mailSender).send(captor.capture());

                SimpleMailMessage message = captor.getValue();
                assertEquals("CANCELAMENTO PROFISSIONAL: Studio Bela Vida - Corte - 01/04/2026", message.getSubject());
                assertTrue(message.getText().contains("Informamos que o atendimento abaixo foi cancelado."));
                assertTrue(message.getText().contains("A agenda foi atualizada com o cancelamento."));
        }

    private Agendamento criarAgendamento() {
        Endereco endereco = new Endereco("Rua das Flores", "123", "Sala 2", "Centro", "São Paulo", "SP", "01310930");
        Estabelecimento estabelecimento = new Estabelecimento(
                1L,
                "Studio Bela Vida",
                endereco,
                List.of(),
                List.of(),
                new Cnpj("04252011000110"),
                List.of(),
                List.of());

        Profissional profissional = new Profissional(
                3L,
                "Bruno Costa",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("bruno@e1.com"),
                null,
                null,
                null,
                Sexo.MASCULINO,
                null);

        Cliente cliente = new Cliente(
                1L,
                "João Silva",
                new Cpf("52998224725"),
                new Celular("11976543210"),
                new Email("joao@cliente.com"),
                Sexo.MASCULINO);

        Servico servico = new Servico(1L, ServicoEnum.CORTE, Duration.ofMinutes(60));

        return new Agendamento(
                48L,
                profissional,
                servico,
                estabelecimento,
                cliente,
                LocalDateTime.of(2026, 4, 1, 17, 30),
                AgendamentoStatus.AGENDADO);
    }

    private Agendamento criarAgendamentoCancelado() {
        Endereco endereco = new Endereco("Rua das Flores", "123", "Sala 2", "Centro", "São Paulo", "SP", "01310930");
        Estabelecimento estabelecimento = new Estabelecimento(
                1L,
                "Studio Bela Vida",
                endereco,
                List.of(),
                List.of(),
                new Cnpj("04252011000110"),
                List.of(),
                List.of());

        Profissional profissional = new Profissional(
                3L,
                "Bruno Costa",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("bruno@e1.com"),
                null,
                null,
                null,
                Sexo.MASCULINO,
                null);

        Cliente cliente = new Cliente(
                1L,
                "João Silva",
                new Cpf("52998224725"),
                new Celular("11976543210"),
                new Email("joao@cliente.com"),
                Sexo.MASCULINO);

        Servico servico = new Servico(1L, ServicoEnum.CORTE, Duration.ofMinutes(60));

        return new Agendamento(
                48L,
                profissional,
                servico,
                estabelecimento,
                cliente,
                LocalDateTime.of(2026, 4, 1, 17, 30),
                AgendamentoStatus.CANCELADO);
    }
}
