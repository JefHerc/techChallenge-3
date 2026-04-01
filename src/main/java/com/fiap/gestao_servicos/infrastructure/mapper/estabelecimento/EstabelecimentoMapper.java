package com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoSearchResult;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.ProfissionalServicoInfo;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoSearchResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.HorarioFuncionamentoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.ProfissionalServicoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.ServicoOfertadoDto;
import com.fiap.gestao_servicos.infrastructure.mapper.DiaSemanaMapper;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.HorarioFuncionamentoEmbeddable;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
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
        response.setUrlFotos(estabelecimento.getUrlFotos());
        response.setHorarioFuncionamento(toHorarioFuncionamentoDtos(estabelecimento.getHorarioFuncionamento()));
        response.setNota(estabelecimento.getNota());
        return response;
    }

    public static EstabelecimentoSearchResponseDto toResponseForSearch(EstabelecimentoSearchResult searchResult) {
        if (searchResult == null || searchResult.getEstabelecimento() == null) {
            return null;
        }

        EstabelecimentoSearchResponseDto response = toResponseForSearch(searchResult.getEstabelecimento());
        if (searchResult.getServicosOfertados() == null || searchResult.getServicosOfertados().isEmpty()) {
            return response;
        }

        List<ServicoOfertadoDto> servicos = searchResult.getServicosOfertados().stream().map(servico -> {
            ServicoOfertadoDto dto = new ServicoOfertadoDto();
            dto.setServicoId(servico.getServicoId());
            dto.setServicoNome(servico.getServicoNome());
            dto.setPreco(servico.getPreco());

            ProfissionalServicoInfo profissional = servico.getProfissional();
            if (profissional != null) {
                ProfissionalServicoDto profissionalDto = new ProfissionalServicoDto();
                profissionalDto.setId(profissional.getId());
                profissionalDto.setNome(profissional.getNome());
                profissionalDto.setEmail(profissional.getEmail());
                profissionalDto.setCelular(profissional.getCelular());
                profissionalDto.setUrlFoto(profissional.getUrlFoto());
                profissionalDto.setNota(profissional.getNota());
                dto.setProfissional(profissionalDto);
            }

            return dto;
        }).toList();

        response.setServicosOfertados(servicos);
        return response;
    }

    public static EstabelecimentoSearchResponseDto toResponseForSearch(
            Estabelecimento estabelecimento,
            List<ServicoProfissionalEntity> servicosOfertados,
            EstabelecimentoRepositoryJpa jpaRepository) {
        if (estabelecimento == null) {
            return null;
        }

        EstabelecimentoSearchResponseDto response = new EstabelecimentoSearchResponseDto();
        response.setId(estabelecimento.getId());
        response.setNome(estabelecimento.getNome());
        response.setEndereco(toEnderecoDto(estabelecimento.getEndereco()));
        response.setUrlFotos(estabelecimento.getUrlFotos());
        response.setHorarioFuncionamento(toHorarioFuncionamentoDtos(estabelecimento.getHorarioFuncionamento()));
        response.setNota(estabelecimento.getNota());

        if (servicosOfertados != null && !servicosOfertados.isEmpty()) {
            List<ServicoOfertadoDto> servicos = new ArrayList<>();
            for (ServicoProfissionalEntity spEntity : servicosOfertados) {
                ServicoOfertadoDto servicoDto = new ServicoOfertadoDto();
                servicoDto.setServicoId(spEntity.getServico().getId());
                servicoDto.setServicoNome(spEntity.getServico().getNome());
                servicoDto.setPreco(spEntity.getValor());

                ProfissionalEntity profEntity = spEntity.getProfissional();
                Double notaProfissional = jpaRepository.findAverageProfissionalNota(profEntity.getId());

                ProfissionalServicoDto profDto = new ProfissionalServicoDto();
                profDto.setId(profEntity.getId());
                profDto.setNome(profEntity.getNome());
                profDto.setEmail(profEntity.getEmail());
                profDto.setCelular(profEntity.getCelular());
                profDto.setUrlFoto(profEntity.getUrlFoto());
                profDto.setNota(notaProfissional);

                servicoDto.setProfissional(profDto);
                servicos.add(servicoDto);
            }
            response.setServicosOfertados(servicos);
        }

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
        entity.setCnpj(domain.getCnpj() != null ? domain.getCnpj().getNormalizedValue() : null);
        entity.setUrlFotos(domain.getUrlFotos() != null ? new ArrayList<>(domain.getUrlFotos()) : new ArrayList<>());
        entity.setHorarioFuncionamento(domain.getHorarioFuncionamento() != null
            ? new ArrayList<>(domain.getHorarioFuncionamento().stream()
                .map(EstabelecimentoMapper::toHorarioFuncionamentoEntity)
                .toList())
            : new ArrayList<>());
        return entity;
    }

    public static EstabelecimentoEntity toEntityForCrud(Estabelecimento domain) {
        return toEntity(domain);
    }

    public static Estabelecimento toDomain(EstabelecimentoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Estabelecimento(
                entity.getId(),
                entity.getNome(),
                toEndereco(entity.getEndereco()),
            null,
            null,
                entity.getCnpj() != null ? new Cnpj(entity.getCnpj()) : null,
                entity.getUrlFotos(),
            entity.getHorarioFuncionamento() != null
                ? entity.getHorarioFuncionamento().stream()
                    .map(EstabelecimentoMapper::toHorarioFuncionamento)
                    .filter(java.util.Objects::nonNull)
                    .toList()
                : null,
                entity.getNota()
        );
    }

    public static Estabelecimento toDomainForCrud(EstabelecimentoEntity entity) {
        return toDomain(entity);
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

        boolean fechado = Boolean.TRUE.equals(dto.isFechado());
        DayOfWeek diaSemana = DiaSemanaMapper.parse(dto.getDiaSemana());
        LocalTime abertura = fechado || dto.getAbertura() == null || dto.getAbertura().isBlank()
            ? null
            : LocalTime.parse(dto.getAbertura());
        LocalTime fechamento = fechado || dto.getFechamento() == null || dto.getFechamento().isBlank()
            ? null
            : LocalTime.parse(dto.getFechamento());

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
        dto.setDiaSemana(DiaSemanaMapper.toPtBr(horario.getDiaSemana()));
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

}