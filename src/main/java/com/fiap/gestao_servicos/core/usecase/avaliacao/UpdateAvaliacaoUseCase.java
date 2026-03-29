package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

public class UpdateAvaliacaoUseCase {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;

    public UpdateAvaliacaoUseCase(AvaliacaoRepository avaliacaoRepository,
                                  AgendamentoRepository agendamentoRepository,
                                  ProfissionalRepository profissionalRepository,
                                  EstabelecimentoRepository estabelecimentoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.profissionalRepository = profissionalRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Avaliacao update(Long id, Avaliacao avaliacao) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Avaliação", id));
        }

        validarReferencias(avaliacao);

        Avaliacao atualizada = avaliacaoRepository.update(id, avaliacao);
        if (atualizada == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Avaliação", id));
        }

        return atualizada;
    }

    private void validarReferencias(Avaliacao avaliacao) {
        Long agendamentoId = validarId(avaliacao.getAgendamento().getId(), "agendamentoId");
        Long profissionalId = validarId(avaliacao.getAgendamento().getProfissional().getId(), "profissionalId");
        Long estabelecimentoId = validarId(avaliacao.getAgendamento().getEstabelecimento().getId(), "estabelecimentoId");

        if (!agendamentoRepository.existsById(agendamentoId)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Agendamento", agendamentoId));
        }

        if (!agendamentoRepository.isConcluido(agendamentoId)) {
            throw new IllegalArgumentException("Agendamento deve estar com status CONCLUIDO para receber avaliação");
        }

        if (!profissionalRepository.existsById(profissionalId)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Profissional", profissionalId));
        }

        if (!estabelecimentoRepository.existsById(estabelecimentoId)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_BY_ID, "Estabelecimento", estabelecimentoId));
        }

        if (!estabelecimentoRepository.temProfissional(estabelecimentoId, profissionalId)) {
            throw new IllegalArgumentException("Profissional informado não pertence ao estabelecimento informado");
        }

        if (!agendamentoRepository.pertenceAoProfissionalEEstabelecimento(agendamentoId, profissionalId, estabelecimentoId)) {
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


