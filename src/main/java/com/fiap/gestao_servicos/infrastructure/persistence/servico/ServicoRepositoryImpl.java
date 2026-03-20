package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicoRepositoryImpl implements ServicoRepository {

    private final ServicoRepositoryJpa servicoRepositoryJpa;

    public ServicoRepositoryImpl(ServicoRepositoryJpa servicoRepositoryJpa) {
        this.servicoRepositoryJpa = servicoRepositoryJpa;
    }

    @Override
    public Servico create(Servico servico) {
        ServicoEntity entity = toEntity(servico);
        ServicoEntity saved = servicoRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Servico update(Long id, Servico servico) {
        ServicoEntity existing = servicoRepositoryJpa.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setNome(servico.getNome());
        existing.setDuracaoMedia(servico.getDuracaoMedia());
        ServicoEntity updated = servicoRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        servicoRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Servico> findAll() {
        return servicoRepositoryJpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Servico findById(Long id) {
        ServicoEntity entity = servicoRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return toDomain(entity);
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
        entity.setNome(servico.getNome());
        entity.setDuracaoMedia(servico.getDuracaoMedia());
        return entity;
    }

    private Servico toDomain(ServicoEntity entity) {
        return new Servico(entity.getId(), entity.getNome(), entity.getDuracaoMedia());
    }
}