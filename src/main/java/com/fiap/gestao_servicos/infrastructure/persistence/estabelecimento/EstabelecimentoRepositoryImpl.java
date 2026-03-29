package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoSearchResult;
import com.fiap.gestao_servicos.core.domain.ProfissionalServicoInfo;
import com.fiap.gestao_servicos.core.domain.ServicoOfertadoSearch;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoMapper;
import com.fiap.gestao_servicos.infrastructure.pagination.SpringPaginationMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;
import org.springframework.data.domain.Page;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class EstabelecimentoRepositoryImpl implements EstabelecimentoRepository {

    private final EstabelecimentoRepositoryJpa jpaRepository;
    private final ProfissionalRepositoryJpa profissionalRepositoryJpa;

    public EstabelecimentoRepositoryImpl(EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa,
                                         ProfissionalRepositoryJpa profissionalRepositoryJpa) {
        this.jpaRepository = estabelecimentoRepositoryJpa;
        this.profissionalRepositoryJpa = profissionalRepositoryJpa;
    }

    @Override
    @Transactional
    public Estabelecimento create(Estabelecimento estabelecimento) {
        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntityForCrud(estabelecimento);
        EstabelecimentoEntity saved = jpaRepository.save(entity);
        return EstabelecimentoMapper.toDomainForCrud(saved);
    }

    @Override
    public boolean existsByCnpjAndNome(String cnpj, String nome) {
        return jpaRepository.existsByCnpjAndNome(normalizeCnpj(cnpj), nome);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return jpaRepository.existsByCnpj(normalizeCnpj(cnpj));
    }

    @Override
    public boolean existsByCnpjAndIdNot(String cnpj, Long id) {
        return jpaRepository.existsByCnpjAndIdNot(normalizeCnpj(cnpj), id);
    }

    @Override
    @Transactional
    public Estabelecimento update(Long id, Estabelecimento estabelecimento) {
        EstabelecimentoEntity existing = jpaRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntityForCrud(estabelecimento);
        existing.setNome(entity.getNome());
        existing.setEndereco(entity.getEndereco());
        existing.setCnpj(entity.getCnpj());
        existing.setUrlFotos(entity.getUrlFotos());
        existing.setHorarioFuncionamento(entity.getHorarioFuncionamento());

        try {
            EstabelecimentoEntity updated = jpaRepository.save(existing);
            return EstabelecimentoMapper.toDomainForCrud(updated);
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

        EstabelecimentoEntity entity = EstabelecimentoMapper.toEntityForCrud(estabelecimento);
        existing.setNome(entity.getNome());
        existing.setEndereco(entity.getEndereco());
        existing.setCnpj(entity.getCnpj());
        existing.setUrlFotos(entity.getUrlFotos());
        existing.setHorarioFuncionamento(entity.getHorarioFuncionamento());

        try {
            EstabelecimentoEntity updated = jpaRepository.save(existing);
            return EstabelecimentoMapper.toDomainForCrud(updated);
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
    public PageResult<Estabelecimento> findAll(PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
                jpaRepository.findAll(SpringPaginationMapper.toPageable(pageQuery))
                        .map(EstabelecimentoMapper::toDomainForCrud));
    }

    @Override
    public Optional<Estabelecimento> findById(Long id) {
        EstabelecimentoEntity entity = jpaRepository.findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(EstabelecimentoMapper.toDomainForCrud(entity));
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean temProfissional(Long estabelecimentoId, Long profissionalId) {
        return profissionalRepositoryJpa.existsByIdAndEstabelecimento_Id(profissionalId, estabelecimentoId);
    }

    @Override
    public PageResult<Estabelecimento> findByCriteria(EstabelecimentoFilter filter, PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
                jpaRepository.findAll(EstabelecimentoSpecification.comFiltros(filter), SpringPaginationMapper.toPageable(pageQuery))
                        .map(entity -> {
                            Double nota = jpaRepository.findAverageNota(entity.getId());
                            entity.setNota(nota);
                            return EstabelecimentoMapper.toDomain(entity);
                        }));
    }

    @Override
    public PageResult<EstabelecimentoSearchResult> findByCriteriaWithServices(
            EstabelecimentoFilter filter, PageQuery pageQuery) {
        org.springframework.data.domain.Pageable pageable = SpringPaginationMapper.toPageable(pageQuery);
        Page<EstabelecimentoEntity> page = jpaRepository.findAll(
            EstabelecimentoSpecification.comFiltros(filter), pageable);

        List<EstabelecimentoSearchResult> content = page.getContent().stream()
            .map(entity -> {
                Double nota = jpaRepository.findAverageNota(entity.getId());
                entity.setNota(nota);
                Estabelecimento domain = EstabelecimentoMapper.toDomain(entity);

                List<ServicoProfissionalEntity> servicosOfertados = null;
                if (filter.getServicoNome() != null && !filter.getServicoNome().isBlank()) {
                    servicosOfertados = jpaRepository.findServicosOfertadosByEstabelecimentoAndServico(
                        entity.getId(), filter.getServicoNome());
                }

                List<ServicoOfertadoSearch> servicos = servicosOfertados == null
                        ? List.of()
                        : servicosOfertados.stream().map(spEntity -> {
                            ProfissionalEntity profEntity = spEntity.getProfissional();
                            Double notaProfissional = jpaRepository.findAverageProfissionalNota(profEntity.getId());

                            ProfissionalServicoInfo profissional = new ProfissionalServicoInfo(
                                    profEntity.getId(),
                                    profEntity.getNome(),
                                    profEntity.getEmail(),
                                    profEntity.getCelular(),
                                    profEntity.getUrlFoto(),
                                    notaProfissional
                            );

                            return new ServicoOfertadoSearch(
                                    spEntity.getServico().getId(),
                                    spEntity.getServico().getNome(),
                                    spEntity.getValor(),
                                    profissional
                            );
                        }).toList();

                return new EstabelecimentoSearchResult(domain, servicos);
            })
            .toList();

        return new PageResult<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                SpringPaginationMapper.toPageResult(page).getSortFields()
        );
    }

    private String normalizeCnpj(String cnpj) {
        if (cnpj == null) {
            return null;
        }
        return cnpj.replaceAll("\\D", "");
    }
}


