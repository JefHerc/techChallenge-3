package com.fiap.gestao_servicos.infrastructure.persistence.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.infrastructure.persistence.*;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoRepositoryJpa;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
    public Agendamento create(Agendamento agendamento) {
        AgendamentoEntity entity = new AgendamentoEntity();
        preencherRelacionamentosEDados(entity, agendamento);
        AgendamentoEntity saved = agendamentoRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
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
    public void deleteById(Long id) {
        agendamentoRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Agendamento> findAll() {
        return agendamentoRepositoryJpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Agendamento findById(Long id) {
        AgendamentoEntity entity = agendamentoRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return toDomain(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return agendamentoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsProfissionalById(Long id) {
        return profissionalRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsServicoById(Long id) {
        return servicoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsEstabelecimentoById(Long id) {
        return estabelecimentoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsClienteById(Long id) {
        return clienteRepositoryJpa.existsById(id);
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
        return new Agendamento(
                entity.getId(),
                toProfissional(entity.getProfissional()),
                toServico(entity.getServico()),
                toEstabelecimento(entity.getEstabelecimento()),
                toCliente(entity.getCliente()),
                entity.getDataHoraInicio(),
                entity.getStatus()
        );
    }

    private Profissional toProfissional(ProfissionalEntity entity) {
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

    private Servico toServico(ServicoEntity entity) {
        return new Servico(entity.getId(), entity.getNome(), entity.getDuracaoMedia());
    }

    private Estabelecimento toEstabelecimento(EstabelecimentoEntity entity) {
        return new Estabelecimento(
                entity.getId(),
                entity.getNome(),
                toEndereco(entity.getEndereco()),
                null,
                null,
                new Cnpj(entity.getCnpj()),
                entity.getUrlFotos(),
                entity.getHorarioFuncionamento()
        );
    }

    private Endereco toEndereco(EnderecoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Endereco(
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep()
        );
    }

    private Cliente toCliente(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNome(),
                new Cpf(entity.getCpf()),
                new Celular(entity.getCelular()),
                new Email(entity.getEmail()),
                entity.getSexo()
        );
    }
}