package com.fiap.gestao_servicos.bdd.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fiap.gestao_servicos.bdd.BddTestServer;
import com.fiap.gestao_servicos.bdd.ScenarioContext;
import io.cucumber.java.en.Given;

import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public class AvaliacaoSteps {

    @Given("que existe um agendamento criado para o estabelecimento {int}")
    public void queExisteUmAgendamentoCriadoParaOEstabelecimento(int estabelecimentoId) throws Exception {
        LocalDateTime dataHora = LocalDateTime.now()
                .plusDays(15 + ThreadLocalRandom.current().nextInt(1, 120))
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        while (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dataHora = dataHora.plusDays(1);
        }

        String body = """
                {
                  "profissionalId": 1,
                  "servicoId": 1,
                  "clienteId": 1,
                                                                        "dataHoraInicio": "%s"
                }
                """.formatted(dataHora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        HttpResponse<String> response = BddTestServer.post(
                "/estabelecimentos/" + estabelecimentoId + "/agendamentos",
                body);

        assertThat(response.statusCode())
                .as("Falha ao criar agendamento de pre-condicao. Resposta: %s", response.body())
                .isEqualTo(201);

        JsonNode json = BddTestServer.objectMapper().readTree(response.body());
        long agendamentoId = json.get("id").asLong();

        String conclusaoBody = """
                {
                  "profissionalId": 1,
                  "servicoId": 1,
                  "clienteId": 1,
                  "dataHoraInicio": "%s",
                  "status": "CONCLUIDO"
                }
                """.formatted(dataHora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        HttpResponse<String> conclusaoResponse = BddTestServer.put(
                "/estabelecimentos/" + estabelecimentoId + "/agendamentos/" + agendamentoId,
                conclusaoBody);

        assertThat(conclusaoResponse.statusCode())
                .as("Falha ao concluir agendamento de pre-condicao. Resposta: %s", conclusaoResponse.body())
                .isEqualTo(200);

        ScenarioContext context = BddTestServer.context();
        context.setLastAgendamentoId(agendamentoId);
        context.setLastEstabelecimentoId(estabelecimentoId);
        context.setLastResponse(conclusaoResponse);
    }
}