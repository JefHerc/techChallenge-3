package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;

public class UpdateAgendamentoUsecase {

    private final AgendamentoRepository agendamentoRepository;

    public UpdateAgendamentoUsecase(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Agendamento update(Long id, Agendamento agendamento) {
        if (!agendamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o id: " + id);
        }

        validarReferencias(agendamento);

        Agendamento atualizado = agendamentoRepository.update(id, agendamento);
        if (atualizado == null) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o id: " + id);
        }

        return atualizado;
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