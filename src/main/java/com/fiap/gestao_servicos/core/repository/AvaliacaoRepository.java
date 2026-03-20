package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.Avaliacao;

import java.util.List;

public interface AvaliacaoRepository {

    Avaliacao create(Avaliacao avaliacao);
    Avaliacao update(Long id, Avaliacao avaliacao);
    void deleteById(Long id);
    List<Avaliacao> findAll();
    Avaliacao findById(Long id);
    boolean existsById(Long id);

    boolean existsAgendamentoById(Long id);
    boolean isAgendamentoConcluido(Long agendamentoId);
    boolean existsProfissionalById(Long id);
    boolean existsEstabelecimentoById(Long id);
    boolean profissionalPertenceAoEstabelecimento(Long profissionalId, Long estabelecimentoId);
    boolean agendamentoPertenceAoProfissionalEEstabelecimento(Long agendamentoId, Long profissionalId, Long estabelecimentoId);

    Agendamento findAgendamentoById(Long id);
}