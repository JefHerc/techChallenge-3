package com.fiap.gestao_servicos.infrastructure.persistence;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.EstabelecimentoDomainToEntityMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.EstabelecimentoEntityToDomainMapper;
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
        jpaRepository.save(entity);
        return EstabelecimentoEntityToDomainMapper.toDomain(entity);
    }

    @Override
    public boolean existsByCnpjAndNome(String cnpj, String nome) {
        return jpaRepository.existsByCnpjAndNome(cnpj, nome);
    }

    @Override
    public Estabelecimento update(Estabelecimento estabelecimento) {
        return null;
    }

    @Override
    public void delete(Estabelecimento estabelecimento) {

    }

    @Override
    public List<Estabelecimento> findAll() {
        return List.of();
    }

    @Override
    public Estabelecimento findById(Long id) {
        return null;
    }
}
