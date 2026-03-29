package com.fiap.gestao_servicos.infrastructure.persistence.avaliacao;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.agendamento.AgendamentoMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AvaliacaoRepositoryImpl implements AvaliacaoRepository {

    private final AvaliacaoRepositoryJpa avaliacaoRepositoryJpa;
    private final AgendamentoRepositoryJpa agendamentoRepositoryJpa;

    public AvaliacaoRepositoryImpl(AvaliacaoRepositoryJpa avaliacaoRepositoryJpa,
                                   AgendamentoRepositoryJpa agendamentoRepositoryJpa) {
        this.avaliacaoRepositoryJpa = avaliacaoRepositoryJpa;
        this.agendamentoRepositoryJpa = agendamentoRepositoryJpa;
    }

    @Override
    @Transactional
    public Avaliacao create(Avaliacao avaliacao) {
        AvaliacaoEntity entity = new AvaliacaoEntity();
        preencherRelacionamentosEDados(entity, avaliacao);
        AvaliacaoEntity saved = avaliacaoRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public Avaliacao update(Long id, Avaliacao avaliacao) {
        AvaliacaoEntity existing = avaliacaoRepositoryJpa.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        preencherRelacionamentosEDados(existing, avaliacao);
        AvaliacaoEntity updated = avaliacaoRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        avaliacaoRepositoryJpa.deleteById(id);
    }

    @Override
    public Page<Avaliacao> findAll(Pageable pageable) {
        return avaliacaoRepositoryJpa.findAll(pageable)
                .map(this::toDomain);
    }

    @Override
    public Page<Avaliacao> findByAgendamentoId(Long agendamentoId, Pageable pageable) {
        return avaliacaoRepositoryJpa.findByAgendamentoId(agendamentoId, pageable)
                .map(this::toDomain);
    }

    @Override
    public Optional<Avaliacao> findById(Long id) {
        AvaliacaoEntity entity = avaliacaoRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
    }

    @Override
    public boolean existsById(Long id) {
        return avaliacaoRepositoryJpa.existsById(id);
    }

    private void preencherRelacionamentosEDados(AvaliacaoEntity entity, Avaliacao avaliacao) {
        AgendamentoEntity agendamento = agendamentoRepositoryJpa.getReferenceById(avaliacao.getAgendamento().getId());

        entity.setAgendamento(agendamento);
        entity.setNotaEstabelecimento(avaliacao.getNotaEstabelecimento());
        entity.setNotaProfissional(avaliacao.getNotaProfissional());
        entity.setComentarioEstabelecimento(avaliacao.getComentarioEstabelecimento());
        entity.setComentarioProfissional(avaliacao.getComentarioProfissional());
    }

    private Avaliacao toDomain(AvaliacaoEntity entity) {
        return new Avaliacao(
                entity.getId(),
                toAgendamentoDomain(entity.getAgendamento()),
                entity.getNotaEstabelecimento(),
                entity.getNotaProfissional(),
                entity.getComentarioEstabelecimento() != null ? entity.getComentarioEstabelecimento() : "",
                entity.getComentarioProfissional() != null ? entity.getComentarioProfissional() : ""
        );
    }

    private Agendamento toAgendamentoDomain(AgendamentoEntity entity) {
        return AgendamentoMapper.toDomain(entity);
    }
}

