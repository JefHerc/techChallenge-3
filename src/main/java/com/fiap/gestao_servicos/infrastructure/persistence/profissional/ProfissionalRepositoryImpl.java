package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.domain.ServicoProfissional;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import com.fiap.gestao_servicos.infrastructure.pagination.SpringPaginationMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoRepositoryJpa;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class ProfissionalRepositoryImpl implements ProfissionalRepository {

    private final ProfissionalRepositoryJpa profissionalRepositoryJpa;
    private final ServicoRepositoryJpa servicoRepositoryJpa;

    public ProfissionalRepositoryImpl(ProfissionalRepositoryJpa profissionalRepositoryJpa,
                                      ServicoRepositoryJpa servicoRepositoryJpa) {
        this.profissionalRepositoryJpa = profissionalRepositoryJpa;
        this.servicoRepositoryJpa = servicoRepositoryJpa;
    }

    @Override
    @Transactional
    public Profissional create(Profissional profissional) {
        return create(null, profissional);
    }

    @Override
    @Transactional
    public Profissional create(Long estabelecimentoId, Profissional profissional) {
        ProfissionalEntity entity = toEntity(profissional);
        if (estabelecimentoId != null) {
            EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
            estabelecimento.setId(estabelecimentoId);
            entity.setEstabelecimento(estabelecimento);
        }
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
        existing.setSexo(profissional.getSexo() != null ? profissional.getSexo().name() : null);

        if (profissional.getExpedientes() != null) {
            Map<String, ExpedienteProfissionalEntity> expedientesExistentesPorDia = new HashMap<>();
            for (ExpedienteProfissionalEntity expedienteExistente : existing.getExpedientes()) {
                if (expedienteExistente.getDiaSemana() != null) {
                    expedientesExistentesPorDia.put(expedienteExistente.getDiaSemana(), expedienteExistente);
                }
            }

            Set<String> diasRecebidos = new HashSet<>();
            profissional.getExpedientes().forEach(e -> {
                String diaSemana = e.getDiaSemana().name();
                ExpedienteProfissionalEntity ee = expedientesExistentesPorDia.get(diaSemana);

                if (ee == null) {
                    ee = new ExpedienteProfissionalEntity();
                    ee.setId(e.getId());
                    ee.setProfissional(existing);
                    ee.setDiaSemana(diaSemana);
                    existing.getExpedientes().add(ee);
                    expedientesExistentesPorDia.put(diaSemana, ee);
                }

                ee.setInicioTurno(e.getInicioTurno());
                ee.setFimTurno(e.getFimTurno());
                ee.setInicioIntervalo(e.getInicioIntervalo());
                ee.setFimIntervalo(e.getFimIntervalo());
                diasRecebidos.add(diaSemana);
            });

            existing.getExpedientes().removeIf(expedienteExistente ->
                    expedienteExistente.getDiaSemana() == null
                            || !diasRecebidos.contains(expedienteExistente.getDiaSemana())
            );
        }

        if (profissional.getServicosProfissional() != null) {
            Map<Long, ServicoProfissionalEntity> existentesPorServicoId = new HashMap<>();
            for (ServicoProfissionalEntity servicoExistente : existing.getServicosProfissional()) {
                if (servicoExistente.getServico() != null && servicoExistente.getServico().getId() != null) {
                    existentesPorServicoId.put(servicoExistente.getServico().getId(), servicoExistente);
                }
            }

            Set<Long> servicosRecebidos = new HashSet<>();
            profissional.getServicosProfissional().forEach(sp -> {
                if (sp.getServico() != null && sp.getServico().getId() != null) {
                    Long servicoId = sp.getServico().getId();
                    ServicoProfissionalEntity spe = existentesPorServicoId.get(servicoId);

                    if (spe == null) {
                        spe = new ServicoProfissionalEntity();
                        spe.setId(sp.getId());
                        spe.setProfissional(existing);

                        spe.setServico(servicoRepositoryJpa.getReferenceById(servicoId));

                        existing.getServicosProfissional().add(spe);
                        existentesPorServicoId.put(servicoId, spe);
                    }

                    spe.setValor(sp.getValor());
                    servicosRecebidos.add(servicoId);
                }
            });

            existing.getServicosProfissional().removeIf(servicoExistente ->
                    servicoExistente.getServico() == null
                            || servicoExistente.getServico().getId() == null
                            || !servicosRecebidos.contains(servicoExistente.getServico().getId())
            );
        }

        ProfissionalEntity updated = profissionalRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        profissionalRepositoryJpa.deleteById(id);
    }

    @Override
        public PageResult<Profissional> findAll(PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
            profissionalRepositoryJpa.findAll(SpringPaginationMapper.toPageable(pageQuery))
                .map(this::toDomain));
    }

    @Override
        public PageResult<Profissional> findPageByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
            profissionalRepositoryJpa.findAllByEstabelecimento_Id(estabelecimentoId, SpringPaginationMapper.toPageable(pageQuery))
                .map(this::toDomain));
    }

    @Override
    public List<Profissional> findAllByEstabelecimentoId(Long estabelecimentoId) {
        return profissionalRepositoryJpa.findAllByEstabelecimento_Id(estabelecimentoId).stream()
                .map(this::toDomain)
                .toList();
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
    public Optional<Profissional> findByIdAndEstabelecimentoId(Long id, Long estabelecimentoId) {
        return profissionalRepositoryJpa.findByIdAndEstabelecimento_Id(id, estabelecimentoId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return profissionalRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsByIdAndEstabelecimentoId(Long id, Long estabelecimentoId) {
        return profissionalRepositoryJpa.existsByIdAndEstabelecimento_Id(id, estabelecimentoId);
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
        return profissionalRepositoryJpa.existsByIdAndServicosProfissionalServicoId(profissionalId, servicoId);
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
                ExpedienteProfissionalEntity ee = new ExpedienteProfissionalEntity();
                ee.setProfissional(entity);
                ee.setDiaSemana(e.getDiaSemana().name());
                ee.setInicioTurno(e.getInicioTurno());
                ee.setFimTurno(e.getFimTurno());
                ee.setInicioIntervalo(e.getInicioIntervalo());
                ee.setFimIntervalo(e.getFimIntervalo());
                return ee;
            }).toList());
        }

        if (profissional.getServicosProfissional() != null) {
            entity.setServicosProfissional(profissional.getServicosProfissional().stream().map(sp -> {
                ServicoProfissionalEntity spe = new ServicoProfissionalEntity();
                spe.setId(sp.getId());
                if (sp.getServico() != null && sp.getServico().getId() != null) {
                    spe.setServico(servicoRepositoryJpa.getReferenceById(sp.getServico().getId()));
                }
                spe.setValor(sp.getValor());
                spe.setProfissional(entity);
                return spe;
            }).toList());
        }

        return entity;
    }

    private Profissional toDomain(ProfissionalEntity entity) {
        List<ExpedienteProfissional> expedientes = null;
        if (entity.getExpedientes() != null) {
            expedientes = entity.getExpedientes().stream().map(ee -> new ExpedienteProfissional(
                    ee.getId(),
                    java.time.DayOfWeek.valueOf(ee.getDiaSemana()),
                    ee.getInicioTurno(),
                    ee.getFimTurno(),
                    ee.getInicioIntervalo(),
                    ee.getFimIntervalo()
            )).toList();
        }

        List<ServicoProfissional> servicosProfissional = null;
        if (entity.getServicosProfissional() != null) {
            servicosProfissional = entity.getServicosProfissional().stream().map(sp -> {
                Servico servico = null;
                if (sp.getServico() != null && sp.getServico().getNome() != null && sp.getServico().getDuracaoMedia() != null) {
                    try {
                        ServicoEnum servicoEnum = ServicoEnum.valueOf(sp.getServico().getNome());
                        servico = new Servico(sp.getServico().getId(), servicoEnum, sp.getServico().getDuracaoMedia());
                    } catch (IllegalArgumentException ignored) {
                        servico = null;
                    }
                }

                return new ServicoProfissional(
                        sp.getId(),
                        servico,
                        null,
                        sp.getValor()
                );
            }).toList();
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
                servicosProfissional
        );
    }
}

