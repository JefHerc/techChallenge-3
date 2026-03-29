package com.fiap.gestao_servicos.infrastructure.mapper.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoProfissional;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ExpedienteProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ProfissionalSearchResponseDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ServicoProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.profissional.ServicoProfissionalResponseDto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public final class ProfissionalMapper {

    private ProfissionalMapper() {
    }

    public static Profissional toDomain(ProfissionalDto dto) {
        if (dto == null) {
            return null;
        }

        return new Profissional(
                null,
                dto.getNome(),
                new Cpf(dto.getCpf()),
                new Celular(dto.getCelular()),
                new Email(dto.getEmail()),
                dto.getUrlFoto(),
                dto.getDescricao(),
                toExpedientes(dto.getExpedientes()),
                parseSexo(dto.getSexo()),
                toServicosProfissional(dto.getServicosProfissional())
        );
    }

    public static ProfissionalResponseDto toResponse(Profissional profissional) {
        if (profissional == null) {
            return null;
        }

        ProfissionalResponseDto response = new ProfissionalResponseDto();
        response.setId(profissional.getId());
        response.setNome(profissional.getNome());
        response.setCpf(profissional.getCpf().getValue());
        response.setCelular(profissional.getCelular().getValue());
        response.setEmail(profissional.getEmail().getValue());
        response.setUrlFoto(profissional.getUrlFoto());
        response.setDescricao(profissional.getDescricao());
        response.setSexo(profissional.getSexo() != null ? profissional.getSexo().name() : null);
        response.setExpedientes(toExpedienteDtos(profissional.getExpedientes()));
        response.setServicosProfissional(toServicoProfissionalResponses(profissional.getServicosProfissional()));
        return response;
    }

    public static ProfissionalSearchResponseDto toResponseForSearch(Profissional profissional) {
        if (profissional == null) {
            return null;
        }

        ProfissionalSearchResponseDto response = new ProfissionalSearchResponseDto();
        response.setId(profissional.getId());
        response.setNome(profissional.getNome());
        response.setUrlFoto(profissional.getUrlFoto());
        response.setDescricao(profissional.getDescricao());
        return response;
    }

    private static List<ExpedienteProfissional> toExpedientes(List<ExpedienteProfissionalDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream().map(ProfissionalMapper::toExpediente).toList();
    }

    private static ExpedienteProfissional toExpediente(ExpedienteProfissionalDto dto) {
        return new ExpedienteProfissional(
                null,
                DayOfWeek.valueOf(dto.getDiaSemana()),
                LocalTime.parse(dto.getInicioTurno()),
                LocalTime.parse(dto.getFimTurno()),
                dto.getInicioIntervalo() != null ? LocalTime.parse(dto.getInicioIntervalo()) : null,
                dto.getFimIntervalo() != null ? LocalTime.parse(dto.getFimIntervalo()) : null
        );
    }

    private static List<ServicoProfissional> toServicosProfissional(List<ServicoProfissionalDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream().map(dto -> new ServicoProfissional(
                null,
                dto.getServicoId() != null ? new Servico(dto.getServicoId(), com.fiap.gestao_servicos.core.domain.ServicoEnum.HIDRATACAO, java.time.Duration.ofMinutes(1)) : null,
                null,
                dto.getValor()
        )).toList();
    }

    private static List<ExpedienteProfissionalDto> toExpedienteDtos(List<ExpedienteProfissional> expedientes) {
        if (expedientes == null) {
            return null;
        }

        return expedientes.stream().map(expediente -> {
            ExpedienteProfissionalDto dto = new ExpedienteProfissionalDto();
            dto.setDiaSemana(expediente.getDiaSemana().name());
            dto.setInicioTurno(expediente.getInicioTurno().toString());
            dto.setFimTurno(expediente.getFimTurno().toString());
            dto.setInicioIntervalo(expediente.getInicioIntervalo() != null ? expediente.getInicioIntervalo().toString() : null);
            dto.setFimIntervalo(expediente.getFimIntervalo() != null ? expediente.getFimIntervalo().toString() : null);
            return dto;
        }).toList();
    }

    private static List<ServicoProfissionalResponseDto> toServicoProfissionalResponses(List<ServicoProfissional> servicosProfissional) {
        if (servicosProfissional == null) {
            return null;
        }

        return servicosProfissional.stream().map(servicoProfissional -> {
            ServicoProfissionalResponseDto dto = new ServicoProfissionalResponseDto();
            dto.setServicoId(servicoProfissional.getServico() != null ? servicoProfissional.getServico().getId() : null);
            dto.setServicoNome(servicoProfissional.getServico() != null ? servicoProfissional.getServico().getNomeDescricao() : null);
            dto.setValor(servicoProfissional.getValor());
            return dto;
        }).toList();
    }

    private static Sexo parseSexo(String sexo) {
        if (sexo == null || sexo.trim().isEmpty()) {
            return null;
        }
        return Sexo.valueOf(sexo.trim().toUpperCase());
    }
}