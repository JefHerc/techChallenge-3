package com.fiap.gestao_servicos.infrastructure.mapper.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.usecase.agendamento.input.AgendamentoInput;
import com.fiap.gestao_servicos.infrastructure.controller.agendamento.AgendamentoCreateDto;
import com.fiap.gestao_servicos.infrastructure.controller.agendamento.AgendamentoDto;
import com.fiap.gestao_servicos.infrastructure.controller.agendamento.AgendamentoResponseDto;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;

import java.time.DayOfWeek;
import java.util.List;

public final class AgendamentoMapper {

    private AgendamentoMapper() {
    }

    public static AgendamentoInput toDomain(AgendamentoDto dto) {
        if (dto == null) {
            return null;
        }

        return new AgendamentoInput(
                dto.getProfissionalId(),
                dto.getServicoId(),
                dto.getEstabelecimentoId(),
                dto.getClienteId(),
                dto.getDataHoraInicio(),
                AgendamentoStatus.parse(dto.getStatus())
        );
    }

    public static AgendamentoInput toCreateDomain(AgendamentoCreateDto dto) {
        if (dto == null) {
            return null;
        }

        return new AgendamentoInput(
                dto.getProfissionalId(),
                dto.getServicoId(),
                dto.getEstabelecimentoId(),
                dto.getClienteId(),
                dto.getDataHoraInicio(),
                AgendamentoStatus.AGENDADO
        );
    }

    public static AgendamentoResponseDto toResponse(Agendamento agendamento) {
        if (agendamento == null) {
            return null;
        }

        AgendamentoResponseDto response = new AgendamentoResponseDto();
        response.setId(agendamento.getId());
        response.setProfissionalId(agendamento.getProfissional().getId());
        response.setProfissionalNome(agendamento.getProfissional().getNome());
        response.setServicoId(agendamento.getServico().getId());
        response.setServicoNome(agendamento.getServico().getNomeDescricao());
        response.setEstabelecimentoId(agendamento.getEstabelecimento().getId());
        response.setEstabelecimentoNome(agendamento.getEstabelecimento().getNome());
        response.setClienteId(agendamento.getCliente().getId());
        response.setClienteNome(agendamento.getCliente().getNome());
        response.setDataHoraInicio(agendamento.getDataHoraInicio());
        response.setDataHoraFim(agendamento.getDataHoraFim());
        response.setStatus(agendamento.getStatus().name());
        return response;
    }

    public static Agendamento toDomain(AgendamentoEntity entity) {
        if (entity == null) {
            return null;
        }

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

    private static Profissional toProfissional(ProfissionalEntity entity) {
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

    private static Servico toServico(ServicoEntity entity) {
        return new Servico(
                entity.getId(),
                java.util.Optional.ofNullable(entity.getNome()).map(ServicoEnum::valueOf).orElse(null),
                entity.getDuracaoMedia()
        );
    }

    private static Estabelecimento toEstabelecimento(EstabelecimentoEntity entity) {
        return new Estabelecimento(
                entity.getId(),
                entity.getNome(),
                toEndereco(entity.getEndereco()),
                null,
                null,
                new Cnpj(entity.getCnpj()),
                entity.getUrlFotos(),
                toHorarioFuncionamento(entity)
        );
    }

    private static List<HorarioFuncionamento> toHorarioFuncionamento(EstabelecimentoEntity entity) {
        if (entity.getHorarioFuncionamento() == null) {
            return null;
        }

        return entity.getHorarioFuncionamento().stream()
                .map(horario -> {
                    if (horario.getDiaSemana() == null || horario.getDiaSemana().isBlank()) {
                        return null;
                    }
                    boolean fechado = Boolean.TRUE.equals(horario.getFechado());
                    if (!fechado && (horario.getAbertura() == null || horario.getFechamento() == null)) {
                        return null;
                    }
                    return new HorarioFuncionamento(
                            null,
                            DayOfWeek.valueOf(horario.getDiaSemana()),
                            fechado ? null : horario.getAbertura(),
                            fechado ? null : horario.getFechamento(),
                            fechado
                    );
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private static Endereco toEndereco(EnderecoEntity entity) {
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

    private static Cliente toCliente(ClienteEntity entity) {
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