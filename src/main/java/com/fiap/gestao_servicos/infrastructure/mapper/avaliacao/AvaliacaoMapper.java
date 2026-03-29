package com.fiap.gestao_servicos.infrastructure.mapper.avaliacao;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.infrastructure.controller.avaliacao.AvaliacaoDto;
import com.fiap.gestao_servicos.infrastructure.controller.avaliacao.AvaliacaoResponseDto;

import java.time.Duration;
import java.time.LocalDateTime;

public final class AvaliacaoMapper {

    private AvaliacaoMapper() {
    }

    public static Avaliacao toDomain(AvaliacaoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Avaliacao(
                null,
                toAgendamento(dto),
                dto.getNotaEstabelecimento(),
                dto.getNotaProfissional(),
                dto.getComentarioEstabelecimento() != null ? dto.getComentarioEstabelecimento() : "",
                dto.getComentarioProfissional() != null ? dto.getComentarioProfissional() : ""
        );
    }

    public static AvaliacaoResponseDto toResponse(Avaliacao avaliacao) {
        if (avaliacao == null) {
            return null;
        }

        AvaliacaoResponseDto response = new AvaliacaoResponseDto();
        response.setId(avaliacao.getId());
        response.setAgendamentoId(avaliacao.getAgendamento().getId());
        response.setProfissionalId(avaliacao.getAgendamento().getProfissional().getId());
        response.setProfissionalNome(avaliacao.getAgendamento().getProfissional().getNome());
        response.setEstabelecimentoId(avaliacao.getAgendamento().getEstabelecimento().getId());
        response.setEstabelecimentoNome(avaliacao.getAgendamento().getEstabelecimento().getNome());
        response.setStatusAgendamento(avaliacao.getAgendamento().getStatus().name());
        response.setNotaEstabelecimento(avaliacao.getNotaEstabelecimento());
        response.setNotaProfissional(avaliacao.getNotaProfissional());
        response.setComentarioEstabelecimento(avaliacao.getComentarioEstabelecimento());
        response.setComentarioProfissional(avaliacao.getComentarioProfissional());
        return response;
    }

    private static Agendamento toAgendamento(AvaliacaoDto dto) {
        return new Agendamento(
                validarId(dto.getAgendamentoId(), "agendamentoId"),
                toProfissional(dto.getProfissionalId()),
                toServicoReferencia(),
                toEstabelecimento(dto.getEstabelecimentoId()),
                toClienteReferencia(),
                LocalDateTime.now(),
                AgendamentoStatus.AGENDADO
        );
    }

    private static Profissional toProfissional(Long id) {
        return new Profissional(validarId(id, "profissionalId"), "Profissional Referência", null, null, null,
                null, null, null, null, null);
    }

    private static Estabelecimento toEstabelecimento(Long id) {
        return new Estabelecimento(validarId(id, "estabelecimentoId"), "Estabelecimento Referência", null,
                null, null, null, null, null);
    }

    private static Servico toServicoReferencia() {
        return new Servico(1L, ServicoEnum.HIDRATACAO, Duration.ofMinutes(1));
    }

    private static Cliente toClienteReferencia() {
        return new Cliente(1L, "Cliente Referência", new Cpf("52998224725"),
                new Celular("11999999999"), new Email("referencia@avaliacao.com"), Sexo.OUTRO);
    }

    private static Long validarId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " deve ser informado com um valor positivo");
        }
        return id;
    }
}