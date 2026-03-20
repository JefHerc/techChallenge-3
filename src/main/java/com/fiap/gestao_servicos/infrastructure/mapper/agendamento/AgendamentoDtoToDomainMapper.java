package com.fiap.gestao_servicos.infrastructure.mapper.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.infrastructure.controller.agendamento.AgendamentoDto;

import java.time.Duration;

public class AgendamentoDtoToDomainMapper {

    public static Agendamento toDomain(AgendamentoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Agendamento(
                null,
                toProfissional(dto.getProfissionalId()),
                toServico(dto.getServicoId()),
                toEstabelecimento(dto.getEstabelecimentoId()),
                toCliente(dto.getClienteId()),
                dto.getDataHoraInicio(),
                parseStatus(dto.getStatus())
        );
    }

    private static Profissional toProfissional(Long id) {
        return new Profissional(validarId(id, "profissionalId"), "Profissional Referência", null, null, null,
                null, null, null, null, null);
    }

    private static Servico toServico(Long id) {
        return new Servico(validarId(id, "servicoId"), "Serviço Referência", Duration.ofMinutes(1));
    }

    private static Estabelecimento toEstabelecimento(Long id) {
        return new Estabelecimento(validarId(id, "estabelecimentoId"), "Estabelecimento Referência", null,
                null, null, null, null, null);
    }

    private static Cliente toCliente(Long id) {
        return new Cliente(validarId(id, "clienteId"), "Cliente Referência", new Cpf("52998224725"),
                new Celular("11999999999"), new Email("referencia@agendamento.com"), Sexo.OUTRO);
    }

    private static Long validarId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " deve ser informado com um valor positivo");
        }
        return id;
    }

    private static AgendamentoStatus parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio");
        }

        try {
            return AgendamentoStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status inválido. Valores permitidos: AGENDADO, CANCELADO, CONCLUIDO");
        }
    }
}