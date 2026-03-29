package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class EstabelecimentoRepositoryImpl implements EstabelecimentoRepository {

    private final EstabelecimentoRepositoryJpa jpaRepository;

    public EstabelecimentoRepositoryImpl(EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa) {
        this.jpaRepository = estabelecimentoRepositoryJpa;
    }

    @Override
    @Transactional
    public Estabelecimento create(Estabelecimento estabelecimento) {
        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntity(estabelecimento);
        EstabelecimentoEntity saved = jpaRepository.save(entity);
        return EstabelecimentoMapper.toDomain(saved);
    }

    @Override
    public boolean existsByCnpjAndNome(String cnpj, String nome) {
        return jpaRepository.existsByCnpjAndNome(cnpj, nome);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return jpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public boolean existsByCnpjAndIdNot(String cnpj, Long id) {
        return jpaRepository.existsByCnpjAndIdNot(cnpj, id);
    }

    @Override
    @Transactional
    public Estabelecimento update(Long id, Estabelecimento estabelecimento) {
        EstabelecimentoEntity existing = jpaRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntity(estabelecimento);
        existing.setNome(entity.getNome());
        existing.setEndereco(entity.getEndereco());
        if (entity.getProfissionais() != null && !entity.getProfissionais().isEmpty()) {
            existing.setProfissionais(entity.getProfissionais());
        }
        if (entity.getServicos() != null && !entity.getServicos().isEmpty()) {
            existing.setServicos(entity.getServicos());
        }
        existing.setCnpj(entity.getCnpj());
        existing.setUrlFotos(entity.getUrlFotos());
        existing.setHorarioFuncionamento(entity.getHorarioFuncionamento());

        try {
            EstabelecimentoEntity updated = jpaRepository.save(existing);
            return EstabelecimentoMapper.toDomain(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateDataException("Dados duplicados ou invalidos ao atualizar estabelecimento/profissionais.");
        }
    }

    @Override
    @Transactional
    public Estabelecimento updateDadosCadastrais(Long id, Estabelecimento estabelecimento) {
        EstabelecimentoEntity existing = jpaRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntity(estabelecimento);
        existing.setNome(entity.getNome());
        existing.setEndereco(entity.getEndereco());
        existing.setCnpj(entity.getCnpj());
        existing.setUrlFotos(entity.getUrlFotos());
        existing.setHorarioFuncionamento(entity.getHorarioFuncionamento());

        try {
            EstabelecimentoEntity updated = jpaRepository.save(existing);
            return EstabelecimentoMapper.toDomain(updated);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateDataException("Dados duplicados ou invalidos ao atualizar estabelecimento.");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Page<Estabelecimento> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
            .map(EstabelecimentoMapper::toDomain);
    }

    @Override
    public Optional<Estabelecimento> findById(Long id) {
        EstabelecimentoEntity entity = jpaRepository.findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(EstabelecimentoMapper.toDomain(entity));
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean temProfissional(Long estabelecimentoId, Long profissionalId) {
        return jpaRepository.existsByIdAndProfissionais_Id(estabelecimentoId, profissionalId);
    }

    @Override
    public Page<Estabelecimento> findByCriteria(EstabelecimentoFilter filter, Pageable pageable) {
        return jpaRepository.findAll(EstabelecimentoSpecification.comFiltros(filter), pageable)
            .map(EstabelecimentoMapper::toDomain);
    }
}


