package com.fiap.gestao_servicos.core.usecase.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.BusinessRuleException;
import com.fiap.gestao_servicos.core.exception.ErrorMessages;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AgendamentoValidator {

    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;

    public AgendamentoValidator(AgendamentoRepository agendamentoRepository,
                                ProfissionalRepository profissionalRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public void validar(Agendamento agendamento,
                        Estabelecimento estabelecimento,
                        Servico servico,
                        Long agendamentoIdIgnorar) {
        validarVinculoProfissionalServico(agendamento.getProfissional().getId(), agendamento.getServico().getId());
        validarHorarioFuncionamentoEstabelecimento(agendamento, estabelecimento, servico);
        validarConflitoAgendaProfissional(agendamento, agendamentoIdIgnorar);
    }

    private void validarVinculoProfissionalServico(Long profissionalId, Long servicoId) {
        if (!profissionalRepository.existsVinculoProfissionalServico(profissionalId, servicoId)) {
            throw new BusinessRuleException(ErrorMessages.PROFISSIONAL_SEM_VINCULO_SERVICO);
        }
    }

    private void validarConflitoAgendaProfissional(Agendamento agendamento, Long agendamentoIdIgnorar) {
        if (agendamento.getStatus() != AgendamentoStatus.AGENDADO) {
            return;
        }

        boolean existeConflito = agendamentoRepository.existsConflitoHorarioProfissional(
                agendamento.getProfissional().getId(),
                agendamento.getServico().getId(),
                agendamento.getDataHoraInicio(),
                agendamentoIdIgnorar
        );

        if (existeConflito) {
            throw new BusinessRuleException(ErrorMessages.CONFLITO_AGENDA_PROFISSIONAL);
        }
    }

    private void validarHorarioFuncionamentoEstabelecimento(Agendamento agendamento,
                                                             Estabelecimento estabelecimento,
                                                             Servico servico) {
        LocalDateTime dataHoraInicio = agendamento.getDataHoraInicio();
        LocalDateTime dataHoraFim = agendamento.getDataHoraFim();
        DayOfWeek diaSemana = dataHoraInicio.getDayOfWeek();

        HorarioFuncionamento horarioDia = estabelecimento.getHorarioFuncionamento().stream()
                .filter(h -> h != null && diaSemana.equals(h.getDiaSemana()))
                .findFirst()
                .orElse(null);

        if (horarioDia == null || horarioDia.isFechado()) {
            throw new BusinessRuleException(ErrorMessages.ESTABELECIMENTO_FECHADO);
        }

        LocalTime inicio = dataHoraInicio.toLocalTime();
        LocalDateTime fechamento = dataHoraInicio.toLocalDate().atTime(horarioDia.getFechamento());
        if (inicio.isBefore(horarioDia.getAbertura()) || dataHoraFim.isAfter(fechamento)) {
            throw new BusinessRuleException(ErrorMessages.HORARIO_FORA_FUNCIONAMENTO);
        }
    }
}