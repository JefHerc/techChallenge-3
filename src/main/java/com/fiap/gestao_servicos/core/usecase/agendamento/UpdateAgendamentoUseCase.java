package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.notification.NotificationPort;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.input.AgendamentoInput;

public class UpdateAgendamentoUseCase {

    private final AgendamentoRepository agendamentoRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;
    private final AgendamentoValidator agendamentoValidator;
        private final NotificationPort notificationPort;

    public UpdateAgendamentoUseCase(
            AgendamentoRepository agendamentoRepository,
            EstabelecimentoRepository estabelecimentoRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
                        ClienteRepository clienteRepository,
                        NotificationPort notificationPort) {
                this(
                                agendamentoRepository,
                                estabelecimentoRepository,
                                servicoRepository,
                                profissionalRepository,
                                clienteRepository,
                                                                notificationPort,
                                new AgendamentoValidator(agendamentoRepository, profissionalRepository)
                );
        }

        public UpdateAgendamentoUseCase(
                        AgendamentoRepository agendamentoRepository,
                        EstabelecimentoRepository estabelecimentoRepository,
                        ServicoRepository servicoRepository,
                        ProfissionalRepository profissionalRepository,
                        ClienteRepository clienteRepository,
                                                NotificationPort notificationPort,
                        AgendamentoValidator agendamentoValidator) {
        this.agendamentoRepository = agendamentoRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.clienteRepository = clienteRepository;
                this.notificationPort = notificationPort;
                this.agendamentoValidator = agendamentoValidator;
    }

    public Agendamento update(Long id, AgendamentoInput input) {
                Agendamento agendamentoAtual = agendamentoRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Agendamento", id)));

        var profissional = profissionalRepository.findByIdAndEstabelecimentoId(
                        input.getProfissionalId(),
                        input.getEstabelecimentoId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Profissional",
                        input.getProfissionalId())));

        var servico = servicoRepository.findByIdAndEstabelecimentoId(
                        input.getServicoId(),
                        input.getEstabelecimentoId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Serviço",
                        input.getServicoId())));

        var estabelecimento = estabelecimentoRepository.findById(input.getEstabelecimentoId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Estabelecimento",
                        input.getEstabelecimentoId())));

        var cliente = clienteRepository.findById(input.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        ErrorMessages.NOT_FOUND_BY_ID,
                        "Cliente",
                        input.getClienteId())));

        Agendamento agendamento = new Agendamento(
                null,
                profissional,
                servico,
                estabelecimento,
                cliente,
                input.getDataHoraInicio(),
                input.getStatus()
        );

        agendamentoValidator.validar(agendamento, estabelecimento, servico, id);

        Agendamento atualizado = agendamentoRepository.update(id, agendamento);
        if (atualizado == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Agendamento", id));
        }

                if (deveNotificarCancelamento(agendamentoAtual, atualizado)) {
                        notificarCancelamento(atualizado);
                }

        return atualizado;
    }

        private boolean deveNotificarCancelamento(Agendamento agendamentoAtual, Agendamento atualizado) {
                return agendamentoAtual.getStatus() != AgendamentoStatus.CANCELADO
                                && atualizado.getStatus() == AgendamentoStatus.CANCELADO;
        }

        private void notificarCancelamento(Agendamento agendamento) {
                notificationPort.send(criarEventoCancelamento(agendamento, LembreteDestinatario.CLIENTE));
                notificationPort.send(criarEventoCancelamento(agendamento, LembreteDestinatario.PROFISSIONAL));
        }

        private LembreteEvento criarEventoCancelamento(Agendamento agendamento, LembreteDestinatario destinatario) {
            return new LembreteEvento(
                    null,
                    agendamento.getId(),
                    null,
                    destinatario,
                    LembreteStatus.PENDENTE,
                    "Agendamento cancelado",
                    obterDestinoEmail(agendamento, destinatario),
                    null,
                    null,
                    null
            );
        }

        private String obterDestinoEmail(Agendamento agendamento, LembreteDestinatario destinatario) {
                if (destinatario == LembreteDestinatario.CLIENTE) {
                        return agendamento.getCliente() != null && agendamento.getCliente().getEmail() != null
                                        ? agendamento.getCliente().getEmail().getValue()
                                        : null;
                }

                return agendamento.getProfissional() != null && agendamento.getProfissional().getEmail() != null
                                ? agendamento.getProfissional().getEmail().getValue()
                                : null;
        }
}


