package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ServicoRepositoryImpl implements ServicoRepository {

    private final ServicoRepositoryJpa servicoRepositoryJpa;

    public ServicoRepositoryImpl(ServicoRepositoryJpa servicoRepositoryJpa) {
        this.servicoRepositoryJpa = servicoRepositoryJpa;
    }

    @Override
    @Transactional
    public Servico create(Servico servico) {
        ServicoEntity entity = toEntity(servico);
        ServicoEntity saved = servicoRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public Servico update(Long id, Servico servico) {
        ServicoEntity existing = servicoRepositoryJpa.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setNome(servico.getNomeAsString());
        existing.setDuracaoMedia(servico.getDuracaoMedia());
        ServicoEntity updated = servicoRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        servicoRepositoryJpa.deleteById(id);
    }

    @Override
    public Page<Servico> findAll(Pageable pageable) {
        return servicoRepositoryJpa.findAll(pageable)
                .map(this::toDomain);
    }

    @Override
    public Optional<Servico> findById(Long id) {
        ServicoEntity entity = servicoRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
    }

    @Override
    public boolean existsById(Long id) {
        return servicoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsByNomeIgnoreCase(String nome) {
        return servicoRepositoryJpa.existsByNomeIgnoreCase(nome);
    }

    private ServicoEntity toEntity(Servico servico) {
        ServicoEntity entity = new ServicoEntity();
        entity.setId(servico.getId());
        entity.setNome(servico.getNomeAsString());
        entity.setDuracaoMedia(servico.getDuracaoMedia());
        return entity;
    }

    private Servico toDomain(ServicoEntity entity) {
        return new Servico(entity.getId(), java.util.Optional.ofNullable(entity.getNome())
                .map(n -> com.fiap.gestao_servicos.core.domain.ServicoEnum.valueOf(n))
                .orElse(null), entity.getDuracaoMedia());
    }
}

