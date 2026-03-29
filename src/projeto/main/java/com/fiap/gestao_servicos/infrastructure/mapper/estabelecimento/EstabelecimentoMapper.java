package com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoSearchResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.HorarioFuncionamentoDto;
import com.fiap.gestao_servicos.infrastructure.mapper.profissional.ProfissionalMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.servico.ServicoMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.HorarioFuncionamentoEmbeddable;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ExpedienteProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public final class EstabelecimentoMapper {

    private EstabelecimentoMapper() {
    }

    public static Estabelecimento toDomain(EstabelecimentoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Estabelecimento(
                null,
                dto.getNome(),
                toEndereco(dto.getEndereco()),
                null,
                null,
                new Cnpj(dto.getCnpj()),
                dto.getUrlFotos(),
                toHorarioFuncionamento(dto.getHorarioFuncionamento())
        );
    }

    public static EstabelecimentoResponseDto toResponse(Estabelecimento estabelecimento) {
        if (estabelecimento == null) {
            return null;
        }

        EstabelecimentoResponseDto response = new EstabelecimentoResponseDto();
        response.setId(estabelecimento.getId());
        response.setNome(estabelecimento.getNome());
        response.setEndereco(toEnderecoDto(estabelecimento.getEndereco()));
        response.setCnpj(estabelecimento.getCnpj() != null ? estabelecimento.getCnpj().getValue() : null);
        response.setUrlFotos(estabelecimento.getUrlFotos());
        response.setHorarioFuncionamento(toHorarioFuncionamentoDtos(estabelecimento.getHorarioFuncionamento()));
        return response;
    }

    public static EstabelecimentoSearchResponseDto toResponseForSearch(Estabelecimento estabelecimento) {
        if (estabelecimento == null) {
            return null;
        }

        EstabelecimentoSearchResponseDto response = new EstabelecimentoSearchResponseDto();
        response.setId(estabelecimento.getId());
        response.setNome(estabelecimento.getNome());
        response.setEndereco(toEnderecoDto(estabelecimento.getEndereco()));
        response.setProfissionais(estabelecimento.getProfissionais().stream().map(ProfissionalMapper::toResponseForSearch).toList());
        response.setServicos(estabelecimento.getServicos().stream().map(ServicoMapper::toResponse).toList());
        response.setUrlFotos(estabelecimento.getUrlFotos());
        response.setHorarioFuncionamento(toHorarioFuncionamentoDtos(estabelecimento.getHorarioFuncionamento()));
        return response;
    }

    public static EstabelecimentoEntity toEntity(Estabelecimento domain) {
        if (domain == null) {
            return null;
        }

        EstabelecimentoEntity entity = new EstabelecimentoEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEndereco(toEnderecoEntity(domain.getEndereco()));
        entity.setProfissionais(domain.getProfissionais() != null ? domain.getProfissionais().stream().map(EstabelecimentoMapper::toProfissionalEntity).toList() : null);
        entity.setServicos(domain.getServicos() != null ? domain.getServicos().stream().map(EstabelecimentoMapper::toServicoEntity).toList() : null);
        entity.setCnpj(domain.getCnpj() != null ? domain.getCnpj().getValue() : null);
        entity.setUrlFotos(domain.getUrlFotos());
        entity.setHorarioFuncionamento(domain.getHorarioFuncionamento() != null ? domain.getHorarioFuncionamento().stream().map(EstabelecimentoMapper::toHorarioFuncionamentoEntity).toList() : null);
        return entity;
    }

    public static Estabelecimento toDomain(EstabelecimentoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Estabelecimento(
                entity.getId(),
                entity.getNome(),
                toEndereco(entity.getEndereco()),
                entity.getProfissionais() != null ? entity.getProfissionais().stream().map(EstabelecimentoMapper::toProfissional).toList() : null,
                entity.getServicos() != null ? entity.getServicos().stream().map(EstabelecimentoMapper::toServico).toList() : null,
                entity.getCnpj() != null ? new Cnpj(entity.getCnpj()) : null,
                entity.getUrlFotos(),
                entity.getHorarioFuncionamento() != null ? entity.getHorarioFuncionamento().stream().map(EstabelecimentoMapper::toHorarioFuncionamento).filter(java.util.Objects::nonNull).toList() : null
        );
    }

    private static Endereco toEndereco(EnderecoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Endereco(
                dto.getLogradouro(),
                dto.getNumero(),
                dto.getComplemento(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getCep()
        );
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

    private static EnderecoDto toEnderecoDto(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        EnderecoDto dto = new EnderecoDto();
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        return dto;
    }

    private static EnderecoEntity toEnderecoEntity(Endereco domain) {
        if (domain == null) {
            return null;
        }

        EnderecoEntity entity = new EnderecoEntity();
        entity.setLogradouro(domain.getLogradouro());
        entity.setNumero(domain.getNumero());
        entity.setComplemento(domain.getComplemento());
        entity.setBairro(domain.getBairro());
        entity.setCidade(domain.getCidade());
        entity.setEstado(domain.getEstado());
        entity.setCep(domain.getCep());
        return entity;
    }

    private static List<HorarioFuncionamento> toHorarioFuncionamento(List<HorarioFuncionamentoDto> dtos) {
        if (dtos == null) {
            return List.of();
        }

        return dtos.stream().map(EstabelecimentoMapper::toHorarioFuncionamento).toList();
    }

    private static HorarioFuncionamento toHorarioFuncionamento(HorarioFuncionamentoDto dto) {
        if (dto == null) {
            return null;
        }

        boolean fechado = dto.isFechado();
        DayOfWeek diaSemana = dto.getDiaSemana() != null ? DayOfWeek.valueOf(dto.getDiaSemana().trim().toUpperCase()) : null;
        LocalTime abertura = fechado || dto.getAbertura() == null || dto.getAbertura().isBlank() ? null : LocalTime.parse(dto.getAbertura());
        LocalTime fechamento = fechado || dto.getFechamento() == null || dto.getFechamento().isBlank() ? null : LocalTime.parse(dto.getFechamento());

        return new HorarioFuncionamento(null, diaSemana, abertura, fechamento, fechado);
    }

    private static List<HorarioFuncionamentoDto> toHorarioFuncionamentoDtos(List<HorarioFuncionamento> horarios) {
        if (horarios == null) {
            return List.of();
        }

        return horarios.stream().map(EstabelecimentoMapper::toHorarioFuncionamentoDto).toList();
    }

    private static HorarioFuncionamentoDto toHorarioFuncionamentoDto(HorarioFuncionamento horario) {
        if (horario == null) {
            return null;
        }

        HorarioFuncionamentoDto dto = new HorarioFuncionamentoDto();
        dto.setDiaSemana(horario.getDiaSemana() != null ? horario.getDiaSemana().name() : null);
        dto.setAbertura(horario.getAbertura() != null ? horario.getAbertura().toString() : null);
        dto.setFechamento(horario.getFechamento() != null ? horario.getFechamento().toString() : null);
        dto.setFechado(horario.isFechado());
        return dto;
    }

    private static HorarioFuncionamentoEmbeddable toHorarioFuncionamentoEntity(HorarioFuncionamento domain) {
        if (domain == null) {
            return null;
        }

        HorarioFuncionamentoEmbeddable entity = new HorarioFuncionamentoEmbeddable();
        entity.setDiaSemana(domain.getDiaSemana() != null ? domain.getDiaSemana().name() : null);
        entity.setAbertura(domain.getAbertura());
        entity.setFechamento(domain.getFechamento());
        entity.setFechado(domain.isFechado());
        return entity;
    }

    private static HorarioFuncionamento toHorarioFuncionamento(HorarioFuncionamentoEmbeddable entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getDiaSemana() == null || entity.getDiaSemana().isBlank()) {
            return null;
        }

        boolean fechado = Boolean.TRUE.equals(entity.getFechado());
        if (!fechado && (entity.getAbertura() == null || entity.getFechamento() == null)) {
            return null;
        }

        return new HorarioFuncionamento(
                null,
                DayOfWeek.valueOf(entity.getDiaSemana()),
                fechado ? null : entity.getAbertura(),
                fechado ? null : entity.getFechamento(),
                fechado
        );
    }

    private static ProfissionalEntity toProfissionalEntity(Profissional domain) {
        if (domain == null) {
            return null;
        }

        ProfissionalEntity entity = new ProfissionalEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCpf(domain.getCpf() != null ? domain.getCpf().getValue() : null);
        entity.setCelular(domain.getCelular() != null ? domain.getCelular().getValue() : null);
        entity.setEmail(domain.getEmail() != null ? domain.getEmail().getNormalized() : null);
        entity.setUrlFoto(domain.getUrlFoto());
        entity.setDescricao(domain.getDescricao());
        entity.setSexo(domain.getSexo() != null ? domain.getSexo().name() : null);
        if (domain.getExpedientes() != null) {
            entity.setExpedientes(domain.getExpedientes().stream().map(EstabelecimentoMapper::toExpedienteEntity).toList());
        }
        return entity;
    }

    private static ExpedienteProfissionalEntity toExpedienteEntity(ExpedienteProfissional expediente) {
        ExpedienteProfissionalEntity entity = new ExpedienteProfissionalEntity();
        entity.setDiaSemana(expediente.getDiaSemana().name());
        entity.setInicioTurno(expediente.getInicioTurno());
        entity.setFimTurno(expediente.getFimTurno());
        entity.setInicioIntervalo(expediente.getInicioIntervalo());
        entity.setFimIntervalo(expediente.getFimIntervalo());
        return entity;
    }

    private static Profissional toProfissional(ProfissionalEntity entity) {
        if (entity == null) {
            return null;
        }

        List<ExpedienteProfissional> expedientes = null;
        if (entity.getExpedientes() != null) {
            expedientes = entity.getExpedientes().stream().map(expediente -> new ExpedienteProfissional(
                    expediente.getId(),
                    DayOfWeek.valueOf(expediente.getDiaSemana()),
                    expediente.getInicioTurno(),
                    expediente.getFimTurno(),
                    expediente.getInicioIntervalo(),
                    expediente.getFimIntervalo()
            )).toList();
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
                null
        );
    }

    private static ServicoEntity toServicoEntity(Servico domain) {
        if (domain == null) {
            return null;
        }

        ServicoEntity entity = new ServicoEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNomeAsString());
        entity.setDuracaoMedia(domain.getDuracaoMedia());
        return entity;
    }

    private static Servico toServico(ServicoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Servico(
                entity.getId(),
                java.util.Optional.ofNullable(entity.getNome()).map(ServicoEnum::valueOf).orElse(null),
                entity.getDuracaoMedia()
        );
    }
}