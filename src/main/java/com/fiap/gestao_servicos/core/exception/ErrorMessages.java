package com.fiap.gestao_servicos.core.exception;

public final class ErrorMessages {

    public static final String NOT_FOUND_BY_ID = "%s não encontrado para o id: %s";
    public static final String PROFISSIONAL_SEM_VINCULO_SERVICO = "Profissional não possui vínculo com o serviço informado";
    public static final String CONFLITO_AGENDA_PROFISSIONAL = "Profissional já possui agendamento no horário informado";
    public static final String ESTABELECIMENTO_FECHADO = "Estabelecimento fechado para o dia informado";
    public static final String HORARIO_FORA_FUNCIONAMENTO = "Horário do agendamento fora do horário de funcionamento do estabelecimento";

    private ErrorMessages() {
    }
}