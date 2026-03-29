package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.usecase.agendamento.input.AgendamentoInput;

public class CreateAgendamentoUseCase {

    private final AgendamentoRepository agendamentoRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;
    private final AgendamentoValidator agendamentoValidator;

    public CreateAgendamentoUseCase(
            AgendamentoRepository agendamentoRepository,
            EstabelecimentoRepository estabelecimentoRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            ClienteRepository clienteRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.clienteRepository = clienteRepository;
        this.agendamentoValidator = new AgendamentoValidator(agendamentoRepository, profissionalRepository);
    }

    public Agendamento create(AgendamentoInput input) {
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

        agendamentoValidator.validar(agendamento, estabelecimento, servico, null);

        return agendamentoRepository.create(agendamento);
    }
}



