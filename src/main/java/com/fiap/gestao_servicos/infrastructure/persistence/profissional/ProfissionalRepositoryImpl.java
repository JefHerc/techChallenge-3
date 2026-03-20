package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfissionalRepositoryImpl implements ProfissionalRepository {

    private final ProfissionalRepositoryJpa profissionalRepositoryJpa;

    public ProfissionalRepositoryImpl(ProfissionalRepositoryJpa profissionalRepositoryJpa) {
        this.profissionalRepositoryJpa = profissionalRepositoryJpa;
    }

    @Override
    public Profissional create(Profissional profissional) {
        ProfissionalEntity entity = toEntity(profissional);
        ProfissionalEntity saved = profissionalRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Profissional update(Long id, Profissional profissional) {
        ProfissionalEntity existing = profissionalRepositoryJpa.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setNome(profissional.getNome());
        existing.setCpf(profissional.getCpf().getValue());
        existing.setCelular(profissional.getCelular().getValue());
        existing.setEmail(profissional.getEmail().getValue());
        existing.setUrlFoto(profissional.getUrlFoto());
        existing.setDescricao(profissional.getDescricao());
        ProfissionalEntity updated = profissionalRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        profissionalRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Profissional> findAll() {
        return profissionalRepositoryJpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Profissional findById(Long id) {
        ProfissionalEntity entity = profissionalRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return toDomain(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return profissionalRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return profissionalRepositoryJpa.existsByCpf(cpf);
    }

    @Override
    public boolean existsByCelular(String celular) {
        return profissionalRepositoryJpa.existsByCelular(celular);
    }

    @Override
    public boolean existsByEmail(String email) {
        return profissionalRepositoryJpa.existsByEmail(email);
    }

    private ProfissionalEntity toEntity(Profissional profissional) {
        ProfissionalEntity entity = new ProfissionalEntity();
        entity.setId(profissional.getId());
        entity.setNome(profissional.getNome());
        entity.setCpf(profissional.getCpf().getValue());
        entity.setCelular(profissional.getCelular().getValue());
        entity.setEmail(profissional.getEmail().getValue());
        entity.setUrlFoto(profissional.getUrlFoto());
        entity.setDescricao(profissional.getDescricao());
        return entity;
    }

    private Profissional toDomain(ProfissionalEntity entity) {
        return new Profissional(
                entity.getId(),
                entity.getNome(),
                new Cpf(entity.getCpf()),
                new Celular(entity.getCelular()),
                new Email(entity.getEmail()),
                entity.getUrlFoto(),
                entity.getDescricao(),
                null,
                null,
                null
        );
    }
}