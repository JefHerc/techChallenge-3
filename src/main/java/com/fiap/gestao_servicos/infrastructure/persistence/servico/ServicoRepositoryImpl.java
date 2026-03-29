package com.fiap.gestao_servicos.infrastructure.persistence.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.ServicoRepository;
import com.fiap.gestao_servicos.infrastructure.pagination.SpringPaginationMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
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
        return create(null, servico);
    }

    @Override
    @Transactional
    public Servico create(Long estabelecimentoId, Servico servico) {
        ServicoEntity entity = toEntity(servico);
        if (estabelecimentoId != null) {
            EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
            estabelecimento.setId(estabelecimentoId);
            entity.setEstabelecimento(estabelecimento);
        }
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
        public PageResult<Servico> findAll(PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
            servicoRepositoryJpa.findAll(SpringPaginationMapper.toPageable(pageQuery))
                .map(this::toDomain));
    }

    @Override
        public PageResult<Servico> findPageByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
            servicoRepositoryJpa.findAllByEstabelecimento_Id(estabelecimentoId, SpringPaginationMapper.toPageable(pageQuery))
                .map(this::toDomain));
    }

    @Override
    public java.util.List<Servico> findAllByEstabelecimentoId(Long estabelecimentoId) {
        return servicoRepositoryJpa.findAllByEstabelecimento_Id(estabelecimentoId).stream()
                .map(this::toDomain)
                .toList();
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
    public Optional<Servico> findByIdAndEstabelecimentoId(Long id, Long estabelecimentoId) {
        return servicoRepositoryJpa.findByIdAndEstabelecimento_Id(id, estabelecimentoId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return servicoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsByIdAndEstabelecimentoId(Long id, Long estabelecimentoId) {
        return servicoRepositoryJpa.existsByIdAndEstabelecimento_Id(id, estabelecimentoId);
    }

    @Override
    public boolean existsByNomeIgnoreCase(String nome) {
        return servicoRepositoryJpa.existsByNomeIgnoreCase(nome);
    }

    @Override
    public boolean existsByNomeIgnoreCaseAndEstabelecimentoId(String nome, Long estabelecimentoId) {
        return servicoRepositoryJpa.existsByNomeIgnoreCaseAndEstabelecimento_Id(nome, estabelecimentoId);
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

