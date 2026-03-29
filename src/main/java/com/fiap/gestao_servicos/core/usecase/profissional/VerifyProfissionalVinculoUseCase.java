package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoProfissional;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class VerifyProfissionalVinculoUseCase {

    private static final String EXPEDIENTE_FORA_DO_HORARIO =
            "Expediente do profissional no dia %s deve estar dentro do horario de funcionamento do estabelecimento";
    private static final String INTERVALO_FORA_DO_HORARIO =
            "Intervalo do profissional no dia %s deve estar dentro do horario de funcionamento do estabelecimento";
    private static final String SERVICO_INVALIDO_PARA_ESTABELECIMENTO =
            "Profissional nao pode vincular servico que nao esta cadastrado no estabelecimento: %s";

    public void verify(Estabelecimento estabelecimento, Profissional profissional) {
        validateExpedientesDentroHorarioFuncionamento(estabelecimento, profissional);
        validateServicosVinculadosAoEstabelecimento(estabelecimento, profissional);
    }

    private void validateExpedientesDentroHorarioFuncionamento(Estabelecimento estabelecimento, Profissional profissional) {
        if (profissional.getExpedientes() == null || profissional.getExpedientes().isEmpty()) {
            return;
        }

        Map<DayOfWeek, HorarioFuncionamento> horariosPorDia = estabelecimento.getHorarioFuncionamento().stream()
                .filter(Objects::nonNull)
                .filter(horario -> horario.getDiaSemana() != null)
                .collect(Collectors.toMap(HorarioFuncionamento::getDiaSemana, horario -> horario, (atual, ignorado) -> atual));

        for (ExpedienteProfissional expediente : profissional.getExpedientes()) {
            HorarioFuncionamento horario = horariosPorDia.get(expediente.getDiaSemana());
            if (horario == null || horario.isFechado()) {
                throw new IllegalArgumentException(String.format(EXPEDIENTE_FORA_DO_HORARIO, expediente.getDiaSemana()));
            }
            if (expediente.getInicioTurno().isBefore(horario.getAbertura())
                    || expediente.getFimTurno().isAfter(horario.getFechamento())) {
                throw new IllegalArgumentException(String.format(EXPEDIENTE_FORA_DO_HORARIO, expediente.getDiaSemana()));
            }
            if (expediente.getInicioIntervalo() != null
                    && (expediente.getInicioIntervalo().isBefore(horario.getAbertura())
                        || expediente.getInicioIntervalo().isAfter(horario.getFechamento()))) {
                throw new IllegalArgumentException(String.format(INTERVALO_FORA_DO_HORARIO, expediente.getDiaSemana()));
            }
            if (expediente.getFimIntervalo() != null
                    && (expediente.getFimIntervalo().isBefore(horario.getAbertura())
                        || expediente.getFimIntervalo().isAfter(horario.getFechamento()))) {
                throw new IllegalArgumentException(String.format(INTERVALO_FORA_DO_HORARIO, expediente.getDiaSemana()));
            }
        }
    }

    private void validateServicosVinculadosAoEstabelecimento(Estabelecimento estabelecimento, Profissional profissional) {
        if (profissional.getServicosProfissional() == null || profissional.getServicosProfissional().isEmpty()) {
            return;
        }

        Set<Long> servicosDoEstabelecimento = estabelecimento.getServicos().stream()
                .filter(Objects::nonNull)
                .map(Servico::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (ServicoProfissional servicoProfissional : profissional.getServicosProfissional()) {
            Long servicoId = servicoProfissional != null && servicoProfissional.getServico() != null
                    ? servicoProfissional.getServico().getId()
                    : null;
            if (servicoId == null || !servicosDoEstabelecimento.contains(servicoId)) {
                throw new IllegalArgumentException(String.format(SERVICO_INVALIDO_PARA_ESTABELECIMENTO, servicoId));
            }
        }
    }
}
