package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ProfissionalRepositoryImpl implements ProfissionalRepository {

    private final ProfissionalRepositoryJpa profissionalRepositoryJpa;

    public ProfissionalRepositoryImpl(ProfissionalRepositoryJpa profissionalRepositoryJpa) {
        this.profissionalRepositoryJpa = profissionalRepositoryJpa;
    }

    @Override
    @Transactional
    public Profissional create(Profissional profissional) {
        ProfissionalEntity entity = toEntity(profissional);
        ProfissionalEntity saved = profissionalRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteById(Long id) {
        profissionalRepositoryJpa.deleteById(id);
    }

    @Override
    public Page<Profissional> findAll(Pageable pageable) {
        return profissionalRepositoryJpa.findAll(pageable)
                .map(this::toDomain);
    }

    @Override
    public Optional<Profissional> findById(Long id) {
        ProfissionalEntity entity = profissionalRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(entity));
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

    @Override
    public boolean existsVinculoProfissionalServico(Long profissionalId, Long servicoId) {
        return profissionalRepositoryJpa.existsByIdAndServicoProfissionalServicoId(profissionalId, servicoId);
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
        entity.setSexo(profissional.getSexo() != null ? profissional.getSexo().name() : null);

        if (profissional.getExpedientes() != null) {
            entity.setExpedientes(profissional.getExpedientes().stream().map(e -> {
                com.fiap.gestao_servicos.infrastructure.persistence.profissional.ExpedienteProfissionalEntity ee = new com.fiap.gestao_servicos.infrastructure.persistence.profissional.ExpedienteProfissionalEntity();
                ee.setDiaSemana(e.getDiaSemana().name());
                ee.setInicioTurno(e.getInicioTurno());
                ee.setFimTurno(e.getFimTurno());
                ee.setInicioIntervalo(e.getInicioIntervalo());
                ee.setFimIntervalo(e.getFimIntervalo());
                return ee;
            }).toList());
        }
        return entity;
    }

    private Profissional toDomain(ProfissionalEntity entity) {
        java.util.List<ExpedienteProfissional> expedientes = null;
        if (entity.getExpedientes() != null) {
            expedientes = entity.getExpedientes().stream().map(ee -> new com.fiap.gestao_servicos.core.domain.ExpedienteProfissional(
                    ee.getId(),
                    java.time.DayOfWeek.valueOf(ee.getDiaSemana()),
                    ee.getInicioTurno(),
                    ee.getFimTurno(),
                    ee.getInicioIntervalo(),
                    ee.getFimIntervalo()
            )).toList();
        }

        return new Profissional(
                entity.getId(),
                entity.getNome(),
                entity.getCpf() != null ? new Cpf(entity.getCpf()) : null,
                entity.getCelular() != null ? new Celular(entity.getCelular()) : null,
                entity.getEmail() != null ? new Email(entity.getEmail()) : null,
                entity.getUrlFoto(),
                entity.getDescricao(),
                expedientes,
                entity.getSexo() != null ? com.fiap.gestao_servicos.core.domain.Sexo.valueOf(entity.getSexo()) : null,
                null
        );
    }
}

