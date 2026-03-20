package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;

public class UpdateAvaliacaoUsecase {

    private final AvaliacaoRepository avaliacaoRepository;

    public UpdateAvaliacaoUsecase(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao update(Long id, Avaliacao avaliacao) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação não encontrada para o id: " + id);
        }

        validarReferencias(avaliacao);

        Avaliacao atualizada = avaliacaoRepository.update(id, avaliacao);
        if (atualizada == null) {
            throw new ResourceNotFoundException("Avaliação não encontrada para o id: " + id);
        }

        return atualizada;
    }

    private void validarReferencias(Avaliacao avaliacao) {
        Long agendamentoId = validarId(avaliacao.getAgendamento().getId(), "agendamentoId");
        Long profissionalId = validarId(avaliacao.getAgendamento().getProfissional().getId(), "profissionalId");
        Long estabelecimentoId = validarId(avaliacao.getAgendamento().getEstabelecimento().getId(), "estabelecimentoId");

        if (!avaliacaoRepository.existsAgendamentoById(agendamentoId)) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o id: " + agendamentoId);
        }

        if (!avaliacaoRepository.isAgendamentoConcluido(agendamentoId)) {
            throw new IllegalArgumentException("Agendamento deve estar com status CONCLUIDO para receber avaliação");
        }

        if (!avaliacaoRepository.existsProfissionalById(profissionalId)) {
            throw new ResourceNotFoundException("Profissional não encontrado para o id: " + profissionalId);
        }

        if (!avaliacaoRepository.existsEstabelecimentoById(estabelecimentoId)) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado para o id: " + estabelecimentoId);
        }

        if (!avaliacaoRepository.profissionalPertenceAoEstabelecimento(profissionalId, estabelecimentoId)) {
            throw new IllegalArgumentException("Profissional informado não pertence ao estabelecimento informado");
        }

        if (!avaliacaoRepository.agendamentoPertenceAoProfissionalEEstabelecimento(agendamentoId, profissionalId, estabelecimentoId)) {
            throw new IllegalArgumentException("Agendamento informado não pertence ao profissional e estabelecimento informados");
        }
    }

    private Long validarId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " deve ser informado com valor positivo");
        }
        return id;
    }
}
