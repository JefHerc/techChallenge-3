package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoDomainToEntityMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoEntityToDomainMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstabelecimentoRepositoryImpl implements EstabelecimentoRepository {

    private final EstabelecimentoRepositoryJpa jpaRepository;

    public EstabelecimentoRepositoryImpl(EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa) {
        this.jpaRepository = estabelecimentoRepositoryJpa;
    }

    @Override
    public Estabelecimento create(Estabelecimento estabelecimento) {
        EstabelecimentoEntity entity = EstabelecimentoDomainToEntityMapper.toEntity(estabelecimento);
        EstabelecimentoEntity saved = jpaRepository.save(entity);
        return EstabelecimentoEntityToDomainMapper.toDomain(saved);
    }

    @Override
    public boolean existsByCnpjAndNome(String cnpj, String nome) {
        return jpaRepository.existsByCnpjAndNome(cnpj, nome);
    }

    @Override
    public Estabelecimento update(Long id, Estabelecimento estabelecimento) {
        EstabelecimentoEntity existing = jpaRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        EstabelecimentoEntity entity = EstabelecimentoDomainToEntityMapper.toEntity(estabelecimento);
        existing.setNome(entity.getNome());
        existing.setEndereco(entity.getEndereco());
        existing.setProfissionais(entity.getProfissionais());
        existing.setServicos(entity.getServicos());
        existing.setCnpj(entity.getCnpj());
        existing.setUrlFotos(entity.getUrlFotos());
        existing.setHorarioFuncionamento(entity.getHorarioFuncionamento());

        EstabelecimentoEntity updated = jpaRepository.save(existing);
        return EstabelecimentoEntityToDomainMapper.toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Estabelecimento> findAll() {
        return jpaRepository.findAll().stream()
                .map(EstabelecimentoEntityToDomainMapper::toDomain)
                .toList();
    }

    @Override
    public Estabelecimento findById(Long id) {
        EstabelecimentoEntity entity = jpaRepository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return EstabelecimentoEntityToDomainMapper.toDomain(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
