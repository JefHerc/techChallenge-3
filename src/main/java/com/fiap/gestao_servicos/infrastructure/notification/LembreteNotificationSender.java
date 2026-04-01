package com.fiap.gestao_servicos.infrastructure.notification;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LembreteNotificationSender implements NotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(LembreteNotificationSender.class);
    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));

    private final JavaMailSender mailSender;
    private final AgendamentoRepository agendamentoRepository;
    private final String from;

    public LembreteNotificationSender(JavaMailSender mailSender,
                                      AgendamentoRepository agendamentoRepository,
                                      @Value("${notification.email.from}") String from) {
        this.mailSender = mailSender;
        this.agendamentoRepository = agendamentoRepository;
        this.from = from;
    }

    @Override
    public void send(LembreteEvento evento) {
        Agendamento agendamento = agendamentoRepository.findById(evento.getAgendamentoId()).orElse(null);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(evento.getDestinoEmail());
        message.setSubject(montarAssunto(evento, agendamento));
        message.setText(montarCorpo(evento, agendamento));

        mailSender.send(message);

        LOGGER.info("[LEMBRETE] tipo={} destinatario={} destino={} agendamentoId={} assunto={} mensagem={}",
                evento.getTipo(),
                evento.getDestinatario(),
                evento.getDestinoEmail(),
                evento.getAgendamentoId(),
                message.getSubject(),
                message.getText());
    }

    private String montarAssunto(LembreteEvento evento, Agendamento agendamento) {
        if (agendamento != null && agendamento.getStatus() == AgendamentoStatus.CANCELADO) {
            return montarAssuntoCancelamento(evento, agendamento);
        }

        String tipoLembrete = formatarTipoLembrete(evento.getTipo());
        String publico = evento.getDestinatario() == LembreteDestinatario.PROFISSIONAL ? "PROFISSIONAL" : "CLIENTE";

        if (agendamento == null || agendamento.getEstabelecimento() == null || agendamento.getServico() == null) {
            return String.format("LEMBRETE %s (%s): Atendimento", publico, tipoLembrete);
        }

        return String.format(
                "LEMBRETE %s (%s): %s - %s - %s",
                publico,
                tipoLembrete,
                agendamento.getEstabelecimento().getNome(),
                agendamento.getServico().getNomeDescricao(),
                agendamento.getDataHoraInicio().format(DATA_FORMATTER));
    }

    private String montarAssuntoCancelamento(LembreteEvento evento, Agendamento agendamento) {
        String publico = evento.getDestinatario() == LembreteDestinatario.PROFISSIONAL ? "PROFISSIONAL" : "CLIENTE";

        if (agendamento.getEstabelecimento() == null || agendamento.getServico() == null) {
            return String.format("CANCELAMENTO %s: Atendimento", publico);
        }

        return String.format(
                "CANCELAMENTO %s: %s - %s - %s",
                publico,
                agendamento.getEstabelecimento().getNome(),
                agendamento.getServico().getNomeDescricao(),
                agendamento.getDataHoraInicio().format(DATA_FORMATTER));
    }

    private String formatarTipoLembrete(LembreteTipo tipo) {
        if (tipo == null) {
            return "N/A";
        }

        return switch (tipo) {
            case T_24_HORAS -> "24H";
            case T_2_HORAS -> "2H";
            case T_30_MINUTOS -> "30MIN";
        };
    }

    private String montarCorpo(LembreteEvento evento, Agendamento agendamento) {
        if (agendamento == null) {
            return evento.getMensagem();
        }

        if (agendamento.getStatus() == AgendamentoStatus.CANCELADO) {
            return montarCorpoCancelamento(evento, agendamento);
        }

        String nomeSaudacao = obterNomeSaudacao(evento, agendamento);
        String estabelecimentoNome = agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getNome()
                : "Não informado";
        String estabelecimentoEndereco = formatarEndereco(agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getEndereco()
                : null);
        String profissionalNome = agendamento.getProfissional() != null
                ? agendamento.getProfissional().getNome()
                : "Não informado";
        String servicoNome = agendamento.getServico() != null
                ? agendamento.getServico().getNomeDescricao()
                : "Não informado";

        if (evento.getDestinatario() == LembreteDestinatario.PROFISSIONAL) {
            return String.format(
                "Olá, %s!%n%n"
                    + "Você tem um atendimento agendado. Confira os detalhes:%n%n"
                    + "Estabelecimento:%n"
                    + "- Nome: %s%n"
                    + "- Endereço: %s%n%n"
                    + "Profissional:%n"
                    + "- Nome: %s%n%n"
                    + "Agendamento:%n"
                    + "- Serviço: %s%n"
                    + "- Início: %s%n"
                    + "- Fim: %s%n%n"
                    + "Ótimo atendimento!",
                nomeSaudacao,
                estabelecimentoNome,
                estabelecimentoEndereco,
                profissionalNome,
                servicoNome,
                agendamento.getDataHoraInicio().format(DATA_HORA_FORMATTER),
                agendamento.getDataHoraFim().format(DATA_HORA_FORMATTER));
        }

        return String.format(
            "Olá, %s!%n%n"
                + "Seu atendimento está chegando. Confira os detalhes:%n%n"
                + "Estabelecimento:%n"
                + "- Nome: %s%n"
                + "- Endereço: %s%n%n"
                + "Profissional:%n"
                + "- Nome: %s%n%n"
                + "Agendamento:%n"
                + "- Serviço: %s%n"
                + "- Início: %s%n"
                + "- Fim: %s%n%n"
                + "Nos vemos em breve!",
            nomeSaudacao,
            estabelecimentoNome,
            estabelecimentoEndereco,
            profissionalNome,
            servicoNome,
            agendamento.getDataHoraInicio().format(DATA_HORA_FORMATTER),
            agendamento.getDataHoraFim().format(DATA_HORA_FORMATTER));
    }

            private String montarCorpoCancelamento(LembreteEvento evento, Agendamento agendamento) {
            String nomeSaudacao = obterNomeSaudacao(evento, agendamento);
            String estabelecimentoNome = agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getNome()
                : "Não informado";
            String estabelecimentoEndereco = formatarEndereco(agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getEndereco()
                : null);
            String profissionalNome = agendamento.getProfissional() != null
                ? agendamento.getProfissional().getNome()
                : "Não informado";
            String servicoNome = agendamento.getServico() != null
                ? agendamento.getServico().getNomeDescricao()
                : "Não informado";

            if (evento.getDestinatario() == LembreteDestinatario.PROFISSIONAL) {
                return String.format(
                    "Olá, %s!%n%n"
                        + "Informamos que o atendimento abaixo foi cancelado.%n%n"
                        + "Estabelecimento:%n"
                        + "- Nome: %s%n"
                        + "- Endereço: %s%n%n"
                        + "Profissional:%n"
                        + "- Nome: %s%n%n"
                        + "Agendamento:%n"
                        + "- Serviço: %s%n"
                        + "- Início: %s%n"
                        + "- Fim: %s%n%n"
                        + "A agenda foi atualizada com o cancelamento.",
                    nomeSaudacao,
                    estabelecimentoNome,
                    estabelecimentoEndereco,
                    profissionalNome,
                    servicoNome,
                    agendamento.getDataHoraInicio().format(DATA_HORA_FORMATTER),
                    agendamento.getDataHoraFim().format(DATA_HORA_FORMATTER));
            }

            return String.format(
                "Olá, %s!%n%n"
                    + "Seu agendamento foi cancelado.%n%n"
                    + "Estabelecimento:%n"
                    + "- Nome: %s%n"
                    + "- Endereço: %s%n%n"
                    + "Profissional:%n"
                    + "- Nome: %s%n%n"
                    + "Agendamento:%n"
                    + "- Serviço: %s%n"
                    + "- Início: %s%n"
                    + "- Fim: %s%n%n"
                    + "Se precisar, faça um novo agendamento quando desejar.",
                nomeSaudacao,
                estabelecimentoNome,
                estabelecimentoEndereco,
                profissionalNome,
                servicoNome,
                agendamento.getDataHoraInicio().format(DATA_HORA_FORMATTER),
                agendamento.getDataHoraFim().format(DATA_HORA_FORMATTER));
            }

    private String obterNomeSaudacao(LembreteEvento evento, Agendamento agendamento) {
        if (evento.getDestinatario() == LembreteDestinatario.PROFISSIONAL && agendamento.getProfissional() != null) {
            return extrairPrimeiroNome(agendamento.getProfissional().getNome());
        }

        if (agendamento.getCliente() != null) {
            return extrairPrimeiroNome(agendamento.getCliente().getNome());
        }

        return "cliente";
    }

    private String extrairPrimeiroNome(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isBlank()) {
            return "cliente";
        }
        String[] partes = nomeCompleto.trim().split("\\s+");
        return partes.length > 0 ? partes[0] : "cliente";
    }

    private String formatarEndereco(Endereco endereco) {
        if (endereco == null) {
            return "Não informado";
        }

        String complemento = (endereco.getComplemento() == null || endereco.getComplemento().isBlank())
                ? ""
                : ", " + endereco.getComplemento();

        return String.format(
                "%s, %s%s - %s, %s/%s - CEP %s",
                endereco.getLogradouro(),
                endereco.getNumero(),
                complemento,
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCepMasked());
    }
}


