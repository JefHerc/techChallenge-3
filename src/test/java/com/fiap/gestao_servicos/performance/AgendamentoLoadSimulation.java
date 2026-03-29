package com.fiap.gestao_servicos.performance;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class AgendamentoLoadSimulation extends Simulation {

    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final String BASE_URL = prop("baseUrl", "http://localhost:8080");
    private static final long ESTABELECIMENTO_ID = longProp("estabelecimentoId", 1L);
        private static final long[] PROFISSIONAL_IDS = longArrayProp("profissionalIds", "1,2,3,4,5");
    private static final long SERVICO_ID = longProp("servicoId", 1L);
    private static final long CLIENTE_ID = longProp("clienteId", 1L);

        private static final int READ_USERS = intProp("readUsers", 80);
        private static final int WRITE_USERS = intProp("writeUsers", 40);
    private static final int RAMP_SECONDS = intProp("rampSeconds", 30);
    private static final int DURATION_SECONDS = intProp("durationSeconds", 120);

    private static final int MAX_P95_MS = intProp("maxP95Ms", 1500);
    private static final double MAX_ERROR_PERCENT = doubleProp("maxErrorPercent", 2.0d);

    private final HttpProtocolBuilder httpProtocol = HttpDsl.http
            .baseUrl(BASE_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private final ScenarioBuilder readScenario = CoreDsl.scenario("Listar Agendamentos")
            .during(DURATION_SECONDS).on(
                    CoreDsl.exec(
                            HttpDsl.http("GET /estabelecimentos/{id}/agendamentos")
                                    .get("/estabelecimentos/" + ESTABELECIMENTO_ID + "/agendamentos?page=0&size=20")
                                    .check(HttpDsl.status().is(200))
                    ).pause(1)
            );

    private final ScenarioBuilder writeScenario = CoreDsl.scenario("Criar Agendamentos Simultaneos")
            .during(DURATION_SECONDS).on(
                    CoreDsl.exec(session -> {
                                int requestSequence = session.contains("requestSequence")
                                        ? session.getInt("requestSequence") + 1
                                        : 0;
                                return session.set("requestSequence", requestSequence);
                            }
                    ).exec(
                            HttpDsl.http("POST /estabelecimentos/{id}/agendamentos")
                                    .post("/estabelecimentos/" + ESTABELECIMENTO_ID + "/agendamentos")
                                    .body(CoreDsl.StringBody(session -> montarPayload(
                                            session.userId(),
                                            session.getInt("requestSequence")
                                    )))
                                    .check(HttpDsl.status().is(201))
                    ).pause(1)
            );

    public AgendamentoLoadSimulation() {
        setUp(
                readScenario.injectOpen(
                        CoreDsl.rampUsers(READ_USERS).during(RAMP_SECONDS)
                ),
                writeScenario.injectOpen(
                        CoreDsl.rampUsers(WRITE_USERS).during(RAMP_SECONDS)
                )
        )
                .protocols(httpProtocol)
                .assertions(
                        CoreDsl.global().failedRequests().percent().lt(MAX_ERROR_PERCENT),
                        CoreDsl.global().responseTime().percentile3().lt(MAX_P95_MS)
                );
    }

        private static String montarPayload(long userId, int requestSequence) {
                long slotIndex = (userId * 10_000L) + requestSequence;
                int profissionalIndex = (int) Math.floorMod(slotIndex, PROFISSIONAL_IDS.length);
                long profissionalId = PROFISSIONAL_IDS[profissionalIndex];

                LocalDateTime inicio = inicioSemConflito(slotIndex)
                .withSecond(0)
                .withNano(0);

        return """
                {
                  "profissionalId": %d,
                  "servicoId": %d,
                  "clienteId": %d,
                  "dataHoraInicio": "%s",
                  "status": "CANCELADO"
                }
                """.formatted(
                profissionalId,
                SERVICO_ID,
                CLIENTE_ID,
                inicio.format(ISO_LOCAL_DATE_TIME)
        );
    }

    private static LocalDateTime inicioSemConflito(long slotIndex) {
                final int slotsPorDia = 10;
        final int intervaloMinutos = 90;

        LocalDateTime base = LocalDateTime.now()
                .plusDays(10)
                .withHour(8)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        long blocoDiario = Math.max(1, PROFISSIONAL_IDS.length) * slotsPorDia;
        long diaOffset = slotIndex / blocoDiario;
        long slotDoDia = (slotIndex / Math.max(1, PROFISSIONAL_IDS.length)) % slotsPorDia;

        return base
                .plusDays(diaOffset)
                .plusMinutes(slotDoDia * intervaloMinutos);
    }

    private static String prop(String key, String defaultValue) {
        String value = System.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    private static int intProp(String key, int defaultValue) {
        String value = System.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : Integer.parseInt(value);
    }

    private static long longProp(String key, long defaultValue) {
        String value = System.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : Long.parseLong(value);
    }

        private static long[] longArrayProp(String key, String defaultValue) {
                long[] values = Arrays.stream(prop(key, defaultValue).split(","))
                                .map(String::trim)
                                .filter(value -> !value.isBlank())
                                .mapToLong(Long::parseLong)
                                .toArray();

                return values.length == 0 ? new long[]{1L} : values;
        }

    private static double doubleProp(String key, double defaultValue) {
        String value = System.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : Double.parseDouble(value);
    }
}
