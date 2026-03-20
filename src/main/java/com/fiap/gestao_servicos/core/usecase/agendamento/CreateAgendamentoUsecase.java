package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class CreateAgendamentoUsecase {

    private final AgendamentoRepository agendamentoRepository;

    public CreateAgendamentoUsecase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Agendamento create(Agendamento agendamento) {
        validarReferencias(agendamento);
        return agendamentoRepository.create(agendamento);
    }

    private void validarReferencias(Agendamento agendamento) {
        Long profissionalId = agendamento.getProfissional().getId();
        if (!agendamentoRepository.existsProfissionalById(profissionalId)) {
            throw new ResourceNotFoundException("Profissional não encontrado para o id: " + profissionalId);
        }

        Long servicoId = agendamento.getServico().getId();
        if (!agendamentoRepository.existsServicoById(servicoId)) {
            throw new ResourceNotFoundException("Serviço não encontrado para o id: " + servicoId);
        }

        Long estabelecimentoId = agendamento.getEstabelecimento().getId();
        if (!agendamentoRepository.existsEstabelecimentoById(estabelecimentoId)) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado para o id: " + estabelecimentoId);
        }

        Long clienteId = agendamento.getCliente().getId();
        if (!agendamentoRepository.existsClienteById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado para o id: " + clienteId);
        }
    }
}