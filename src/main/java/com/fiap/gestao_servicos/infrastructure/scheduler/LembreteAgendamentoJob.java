package com.fiap.gestao_servicos.infrastructure.scheduler;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import com.fiap.gestao_servicos.core.repository.LembreteEventoRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentosParaLembreteUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LembreteAgendamentoJob {

    private static final Logger LOG = LoggerFactory.getLogger(LembreteAgendamentoJob.class);

    private final FindAgendamentosParaLembreteUseCase findAgendamentosParaLembreteUseCase;
    private final LembreteEventoRepository lembreteEventoRepository;
    private final NotificationPort notificationPort;
    private final long lembreteDelayMillis;

    private volatile LocalDateTime ultimaExecucao;

    public LembreteAgendamentoJob(FindAgendamentosParaLembreteUseCase findAgendamentosParaLembreteUseCase,
                                   LembreteEventoRepository lembreteEventoRepository,
                                   NotificationPort notificationPort,
                                   @Value("${scheduler.lembrete.delay:60000}") long lembreteDelayMillis) {
        this.findAgendamentosParaLembreteUseCase = findAgendamentosParaLembreteUseCase;
        this.lembreteEventoRepository = lembreteEventoRepository;
        this.notificationPort = notificationPort;
        this.lembreteDelayMillis = lembreteDelayMillis;
    }

    @Scheduled(fixedDelayString = "${scheduler.lembrete.delay:60000}")
    @Transactional
    public void processarLembretes() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime referenciaInicial = obterInicioDaJanela(agora);

        LOG.info("[SCHEDULER][LEMBRETE] Iniciando busca de lembretes. janelaInicio={} janelaFim={} delayMs={}",
                referenciaInicial,
                agora,
                lembreteDelayMillis);

        try {
            gerarEventosPorJanela(LembreteTipo.T_24_HORAS, 24 * 60, referenciaInicial, agora);
            gerarEventosPorJanela(LembreteTipo.T_2_HORAS, 2 * 60, referenciaInicial, agora);
            gerarEventosPorJanela(LembreteTipo.T_30_MINUTOS, 30, referenciaInicial, agora);

            List<LembreteEvento> pendentes = lembreteEventoRepository.findTop200Pendentes();

            for (LembreteEvento evento : pendentes) {
                try {
                    LOG.info("[SCHEDULER][LEMBRETE] Enviando lembrete agendamentoId={} destinatario={} email={} tipo={}",
                            evento.getAgendamentoId(),
                            evento.getDestinatario(),
                            evento.getDestinoEmail(),
                            evento.getTipo());
                    notificationPort.send(evento);
                    lembreteEventoRepository.save(evento.marcarComoEnviado(LocalDateTime.now()));
                } catch (Exception ex) {
                    lembreteEventoRepository.save(evento.marcarComoFalha(ex.getMessage()));
                }
            }
        } finally {
            ultimaExecucao = agora;
        }
    }

    private void gerarEventosPorJanela(LembreteTipo tipo,
                                       long minutosAntes,
                                       LocalDateTime referenciaInicial,
                                       LocalDateTime referenciaFinal) {
        // Cobre agendamentos futuros na janela de antecedencia, incluindo criacoes tardias e reinicios do job.
        LocalDateTime inicioAlvo = referenciaInicial;
        LocalDateTime fimAlvo = referenciaFinal.plusMinutes(minutosAntes);
        String modoJanela = "JANELA_ATE_ANTECEDENCIA";

        List<Agendamento> agendamentos = findAgendamentosParaLembreteUseCase
            .findByJanela(AgendamentoStatus.AGENDADO, inicioAlvo, fimAlvo);

        LOG.info("[SCHEDULER][LEMBRETE] Busca concluida tipo={} modo={} janelaAlvoInicio={} janelaAlvoFim={} encontrados={}",
                tipo,
                modoJanela,
                inicioAlvo,
                fimAlvo,
                agendamentos.size());

        for (Agendamento agendamento : agendamentos) {
            criarEventoSeNaoExiste(agendamento, tipo, LembreteDestinatario.CLIENTE);
            criarEventoSeNaoExiste(agendamento, tipo, LembreteDestinatario.PROFISSIONAL);
        }
    }

    private LocalDateTime obterInicioDaJanela(LocalDateTime agora) {
        if (ultimaExecucao != null) {
            return ultimaExecucao;
        }

        return agora.minusNanos(lembreteDelayMillis * 1_000_000);
    }

    private void criarEventoSeNaoExiste(Agendamento agendamento,
                                        LembreteTipo tipo,
                                        LembreteDestinatario destinatario) {
        boolean jaExiste = lembreteEventoRepository
                .existsByAgendamentoIdAndTipoAndDestinatario(agendamento.getId(), tipo, destinatario);

        if (jaExiste) {
            return;
        }

        String emailDestino = obterEmailDestino(agendamento, destinatario);

        LOG.info("[SCHEDULER][LEMBRETE] Lembrete pendente criado agendamentoId={} dataHoraInicio={} destinatario={} email={} tipo={}",
            agendamento.getId(),
            agendamento.getDataHoraInicio(),
            destinatario,
            emailDestino,
            tipo);

        LembreteEvento evento = new LembreteEvento(
                null,
                agendamento.getId(),
                tipo,
                destinatario,
                LembreteStatus.PENDENTE,
                montarMensagem(agendamento, tipo),
                emailDestino,
                LocalDateTime.now(),
                null,
                null
        );

        lembreteEventoRepository.save(evento);
    }

    private String obterEmailDestino(Agendamento agendamento, LembreteDestinatario destinatario) {
        if (destinatario == LembreteDestinatario.CLIENTE) {
            return agendamento.getCliente() != null && agendamento.getCliente().getEmail() != null
                    ? agendamento.getCliente().getEmail().getValue()
                    : null;
        }

        return agendamento.getProfissional() != null && agendamento.getProfissional().getEmail() != null
                ? agendamento.getProfissional().getEmail().getValue()
                : null;
    }

    private String montarMensagem(Agendamento agendamento, LembreteTipo tipo) {
        String servicoNome = agendamento.getServico().getNomeDescricao();

        return "Lembrete " + tipo + ": atendimento de "
                + servicoNome
                + " em " + agendamento.getDataHoraInicio()
                + " no estabelecimento " + agendamento.getEstabelecimento().getNome() + ".";
    }
}



