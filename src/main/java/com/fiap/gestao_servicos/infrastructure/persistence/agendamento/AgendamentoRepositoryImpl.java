package com.fiap.gestao_servicos.infrastructure.persistence.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.infrastructure.mapper.agendamento.AgendamentoMapper;
import com.fiap.gestao_servicos.infrastructure.pagination.SpringPaginationMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoRepositoryJpa;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class AgendamentoRepositoryImpl implements AgendamentoRepository {

    private final AgendamentoRepositoryJpa agendamentoRepositoryJpa;
    private final ProfissionalRepositoryJpa profissionalRepositoryJpa;
    private final ServicoRepositoryJpa servicoRepositoryJpa;
    private final EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa;
    private final ClienteRepositoryJpa clienteRepositoryJpa;

    public AgendamentoRepositoryImpl(AgendamentoRepositoryJpa agendamentoRepositoryJpa,
                                     ProfissionalRepositoryJpa profissionalRepositoryJpa,
                                     ServicoRepositoryJpa servicoRepositoryJpa,
                                     EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa,
                                     ClienteRepositoryJpa clienteRepositoryJpa) {
        this.agendamentoRepositoryJpa = agendamentoRepositoryJpa;
        this.profissionalRepositoryJpa = profissionalRepositoryJpa;
        this.servicoRepositoryJpa = servicoRepositoryJpa;
        this.estabelecimentoRepositoryJpa = estabelecimentoRepositoryJpa;
        this.clienteRepositoryJpa = clienteRepositoryJpa;
    }

    @Override
    @Transactional
    public Agendamento create(Agendamento agendamento) {
        AgendamentoEntity entity = new AgendamentoEntity();
        preencherRelacionamentosEDados(entity, agendamento);
        AgendamentoEntity saved = agendamentoRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public Agendamento update(Long id, Agendamento agendamento) {
        AgendamentoEntity existing = agendamentoRepositoryJpa.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        preencherRelacionamentosEDados(existing, agendamento);
        AgendamentoEntity updated = agendamentoRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        agendamentoRepositoryJpa.deleteById(id);
    }

    @Override
        public PageResult<Agendamento> findAll(PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
            agendamentoRepositoryJpa.findAll(SpringPaginationMapper.toPageable(pageQuery))
                .map(this::toDomain));
    }

    @Override
        public PageResult<Agendamento> findByEstabelecimentoId(Long estabelecimentoId, PageQuery pageQuery) {
        return SpringPaginationMapper.toPageResult(
            agendamentoRepositoryJpa.findByEstabelecimentoId(estabelecimentoId, SpringPaginationMapper.toPageable(pageQuery))
                .map(this::toDomain));
    }

    @Override
    public List<Agendamento> findByStatusAndDataHoraInicioBetween(AgendamentoStatus status,
                                                                   LocalDateTime inicio,
                                                                   LocalDateTime fim) {
        return agendamentoRepositoryJpa.findByStatusAndDataHoraInicioBetween(status, inicio, fim).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Agendamento> findById(Long id) {
        return agendamentoRepositoryJpa.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return agendamentoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean isConcluido(Long agendamentoId) {
        return agendamentoRepositoryJpa.existsByIdAndStatus(agendamentoId, AgendamentoStatus.CONCLUIDO);
    }

    @Override
    public boolean pertenceAoProfissionalEEstabelecimento(Long agendamentoId, Long profissionalId, Long estabelecimentoId) {
        return agendamentoRepositoryJpa.existsByIdAndProfissionalIdAndEstabelecimentoId(
                agendamentoId,
                profissionalId,
                estabelecimentoId
        );
    }

    @Override
    public boolean existsConflitoHorarioProfissional(Long profissionalId,
                                                     Long servicoId,
                                                     LocalDateTime dataHoraInicio,
                                                     Long agendamentoIdIgnorar) {
        if (profissionalId == null || servicoId == null || dataHoraInicio == null) {
            return false;
        }

        ServicoEntity servico = servicoRepositoryJpa.findById(servicoId).orElse(null);
        if (servico == null || servico.getDuracaoMedia() == null) {
            return false;
        }

        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(servico.getDuracaoMedia().toMinutes());

        return agendamentoRepositoryJpa.existsConflitoHorarioProfissional(
                profissionalId,
                dataHoraInicio,
                dataHoraFim,
                AgendamentoStatus.AGENDADO,
                agendamentoIdIgnorar
        );
    }

    private void preencherRelacionamentosEDados(AgendamentoEntity entity, Agendamento agendamento) {
        ProfissionalEntity profissional = profissionalRepositoryJpa.getReferenceById(agendamento.getProfissional().getId());
        ServicoEntity servico = servicoRepositoryJpa.getReferenceById(agendamento.getServico().getId());
        EstabelecimentoEntity estabelecimento = estabelecimentoRepositoryJpa.getReferenceById(agendamento.getEstabelecimento().getId());
        ClienteEntity cliente = clienteRepositoryJpa.getReferenceById(agendamento.getCliente().getId());

        entity.setProfissional(profissional);
        entity.setServico(servico);
        entity.setEstabelecimento(estabelecimento);
        entity.setCliente(cliente);
        entity.setDataHoraInicio(agendamento.getDataHoraInicio());
        entity.setDataHoraFim(calcularDataHoraFim(agendamento.getDataHoraInicio(), servico));
        entity.setStatus(agendamento.getStatus());
    }

    private LocalDateTime calcularDataHoraFim(LocalDateTime dataHoraInicio, ServicoEntity servico) {
        if (dataHoraInicio == null) {
            throw new IllegalArgumentException("Data/Hora de início não pode ser nula");
        }
        if (servico == null || servico.getDuracaoMedia() == null) {
            throw new IllegalArgumentException("Serviço informado não possui duração média válida");
        }
        return dataHoraInicio.plusMinutes(servico.getDuracaoMedia().toMinutes());
    }

    private Agendamento toDomain(AgendamentoEntity entity) {
        return AgendamentoMapper.toDomain(entity);
    }
}


