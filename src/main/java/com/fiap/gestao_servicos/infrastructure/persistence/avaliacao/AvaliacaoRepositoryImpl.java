package com.fiap.gestao_servicos.infrastructure.persistence.avaliacao;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AvaliacaoRepositoryImpl implements AvaliacaoRepository {

    private final AvaliacaoRepositoryJpa avaliacaoRepositoryJpa;
    private final AgendamentoRepositoryJpa agendamentoRepositoryJpa;
    private final ProfissionalRepositoryJpa profissionalRepositoryJpa;
    private final EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa;

    public AvaliacaoRepositoryImpl(AvaliacaoRepositoryJpa avaliacaoRepositoryJpa,
                                   AgendamentoRepositoryJpa agendamentoRepositoryJpa,
                                   ProfissionalRepositoryJpa profissionalRepositoryJpa,
                                   EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa) {
        this.avaliacaoRepositoryJpa = avaliacaoRepositoryJpa;
        this.agendamentoRepositoryJpa = agendamentoRepositoryJpa;
        this.profissionalRepositoryJpa = profissionalRepositoryJpa;
        this.estabelecimentoRepositoryJpa = estabelecimentoRepositoryJpa;
    }

    @Override
    public Avaliacao create(Avaliacao avaliacao) {
        AvaliacaoEntity entity = new AvaliacaoEntity();
        preencherRelacionamentosEDados(entity, avaliacao);
        AvaliacaoEntity saved = avaliacaoRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Avaliacao update(Long id, Avaliacao avaliacao) {
        AvaliacaoEntity existing = avaliacaoRepositoryJpa.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        preencherRelacionamentosEDados(existing, avaliacao);
        AvaliacaoEntity updated = avaliacaoRepositoryJpa.save(existing);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        avaliacaoRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Avaliacao> findAll() {
        return avaliacaoRepositoryJpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Avaliacao findById(Long id) {
        AvaliacaoEntity entity = avaliacaoRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return toDomain(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return avaliacaoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsAgendamentoById(Long id) {
        return agendamentoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean isAgendamentoConcluido(Long agendamentoId) {
        AgendamentoEntity agendamento = agendamentoRepositoryJpa.findById(agendamentoId).orElse(null);
        return agendamento != null && agendamento.getStatus() == AgendamentoStatus.CONCLUIDO;
    }

    @Override
    public boolean existsProfissionalById(Long id) {
        return profissionalRepositoryJpa.existsById(id);
    }

    @Override
    public boolean existsEstabelecimentoById(Long id) {
        return estabelecimentoRepositoryJpa.existsById(id);
    }

    @Override
    public boolean profissionalPertenceAoEstabelecimento(Long profissionalId, Long estabelecimentoId) {
        return estabelecimentoRepositoryJpa.existsByIdAndProfissionais_Id(estabelecimentoId, profissionalId);
    }

    @Override
    public boolean agendamentoPertenceAoProfissionalEEstabelecimento(Long agendamentoId, Long profissionalId, Long estabelecimentoId) {
        return avaliacaoRepositoryJpa.existsAgendamentoByIdAndProfissionalAndEstabelecimento(
                agendamentoId,
                profissionalId,
                estabelecimentoId
        );
    }

    @Override
    public Agendamento findAgendamentoById(Long id) {
        AgendamentoEntity entity = agendamentoRepositoryJpa.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return toAgendamentoDomain(entity);
    }

    private void preencherRelacionamentosEDados(AvaliacaoEntity entity, Avaliacao avaliacao) {
        AgendamentoEntity agendamento = agendamentoRepositoryJpa.getReferenceById(avaliacao.getAgendamento().getId());

        entity.setAgendamento(agendamento);
        entity.setNotaEstabelecimento(avaliacao.getNotaEstabelecimento());
        entity.setNotaProfissional(avaliacao.getNotaProfissional());
        entity.setComentarioEstabelecimento(avaliacao.getComentarioEstabelecimento());
        entity.setComentarioProfissional(avaliacao.getComentarioProfissional());
    }

    private Avaliacao toDomain(AvaliacaoEntity entity) {
        return new Avaliacao(
                entity.getId(),
                toAgendamentoDomain(entity.getAgendamento()),
                entity.getNotaEstabelecimento(),
                entity.getNotaProfissional(),
                entity.getComentarioEstabelecimento() != null ? entity.getComentarioEstabelecimento() : "",
                entity.getComentarioProfissional() != null ? entity.getComentarioProfissional() : ""
        );
    }

    private Agendamento toAgendamentoDomain(AgendamentoEntity entity) {
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