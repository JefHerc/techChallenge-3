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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LembreteAgendamentoJob {

    private static final long JANELA_MINUTOS = 1;

    private final FindAgendamentosParaLembreteUseCase findAgendamentosParaLembreteUseCase;
    private final LembreteEventoRepository lembreteEventoRepository;
    private final NotificationPort notificationPort;

    public LembreteAgendamentoJob(FindAgendamentosParaLembreteUseCase findAgendamentosParaLembreteUseCase,
                                   LembreteEventoRepository lembreteEventoRepository,
                                   NotificationPort notificationPort) {
        this.findAgendamentosParaLembreteUseCase = findAgendamentosParaLembreteUseCase;
        this.lembreteEventoRepository = lembreteEventoRepository;
        this.notificationPort = notificationPort;
    }

    @Scheduled(fixedDelayString = "${scheduler.lembrete.delay:300000}")
    @Transactional
    public void processarLembretes() {
        gerarEventosPorJanela(LembreteTipo.T_24_HORAS, 24 * 60);
        gerarEventosPorJanela(LembreteTipo.T_2_HORAS, 2 * 60);
        gerarEventosPorJanela(LembreteTipo.T_30_MINUTOS, 30);

        List<LembreteEvento> pendentes = lembreteEventoRepository.findTop200Pendentes();

        for (LembreteEvento evento : pendentes) {
            try {
                notificationPort.send(evento);
                evento.setStatus(LembreteStatus.ENVIADO);
                evento.setEnviadoEm(LocalDateTime.now());
                evento.setErro(null);
                lembreteEventoRepository.save(evento);
            } catch (Exception ex) {
                evento.setStatus(LembreteStatus.FALHA);
                evento.setErro(ex.getMessage());
                lembreteEventoRepository.save(evento);
            }
        }
    }

    private void gerarEventosPorJanela(LembreteTipo tipo, long minutosAntes) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioAlvo = agora.plusMinutes(minutosAntes);
        LocalDateTime fimAlvo = inicioAlvo.plusMinutes(JANELA_MINUTOS);

        List<Agendamento> agendamentos = findAgendamentosParaLembreteUseCase
            .findByJanela(AgendamentoStatus.AGENDADO, inicioAlvo, fimAlvo);

        for (Agendamento agendamento : agendamentos) {
            criarEventoSeNaoExiste(agendamento, tipo, LembreteDestinatario.CLIENTE);
            criarEventoSeNaoExiste(agendamento, tipo, LembreteDestinatario.PROFISSIONAL);
        }
    }

    private void criarEventoSeNaoExiste(Agendamento agendamento,
                                        LembreteTipo tipo,
                                        LembreteDestinatario destinatario) {
        boolean jaExiste = lembreteEventoRepository
                .existsByAgendamentoIdAndTipoAndDestinatario(agendamento.getId(), tipo, destinatario);

        if (jaExiste) {
            return;
        }

        LembreteEvento evento = new LembreteEvento();
        evento.setAgendamentoId(agendamento.getId());
        evento.setTipo(tipo);
        evento.setDestinatario(destinatario);
        evento.setStatus(LembreteStatus.PENDENTE);
        evento.setCriadoEm(LocalDateTime.now());
        evento.setMensagem(montarMensagem(agendamento, tipo));

        lembreteEventoRepository.save(evento);
    }

    private String montarMensagem(Agendamento agendamento, LembreteTipo tipo) {
        String servicoNome = agendamento.getServico().getNomeDescricao();

        return "Lembrete " + tipo + ": atendimento de "
                + servicoNome
                + " em " + agendamento.getDataHoraInicio()
                + " no estabelecimento " + agendamento.getEstabelecimento().getNome() + ".";
    }
}



