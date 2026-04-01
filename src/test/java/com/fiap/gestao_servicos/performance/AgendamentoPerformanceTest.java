package com.fiap.gestao_servicos.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalRepositoryJpa;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoRepositoryJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("bdd")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class AgendamentoPerformanceTest {

    private static final int CARGA_CONCORRENTE = 48;
    private static final int CARGA_VOLUME_ALTO = 80;
    private static final int REPETICOES_LEITURA = 12;
    private static final int TAMANHO_PAGINA = 50;
    private static final int DIAS_OFFSET_VOLUME_ALTO = 60;
    private static final long MAX_TEMPO_TOTAL_CARGA_MS = 15000;
    private static final long MAX_P95_POST_MS = 2500;
    private static final long MAX_MEDIA_GET_MS = 700;
    private static final long MAX_PIOR_GET_MS = 1500;
    private static final List<Integer> HORARIOS_DISPONIVEIS = List.of(12, 13, 14, 15);

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    private EstabelecimentoRepositoryJpa estabelecimentoRepositoryJpa;

    @Autowired
    private ServicoRepositoryJpa servicoRepositoryJpa;

    @Autowired
    private ProfissionalRepositoryJpa profissionalRepositoryJpa;

    @Autowired
    private ClienteRepositoryJpa clienteRepositoryJpa;

    @Autowired
    private AgendamentoRepositoryJpa agendamentoRepositoryJpa;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Test
    @Timeout(45)
    @DisplayName("Deve suportar carga simultânea de agendamentos dentro do SLA")
    void deveSuportarCargaSimultaneaDeAgendamentosDentroDoSla() {
        TestFixture fixture = carregarFixture();
        long totalAntes = agendamentoRepositoryJpa.count();

        BatchMetrics metricas = executarCargaSimultanea(fixture, CARGA_CONCORRENTE, 0);

        long totalDepois = agendamentoRepositoryJpa.count();
        List<String> falhasHttp = metricas.requestMetrics().stream()
                .filter(metric -> metric.statusCode() != 201)
                .map(metric -> metric.statusCode() + ": " + metric.body())
                .toList();

        assertEquals(CARGA_CONCORRENTE, metricas.sucessos(),
                () -> "Nem todos os agendamentos foram criados com sucesso. Falhas: " + falhasHttp);
        assertEquals(CARGA_CONCORRENTE, totalDepois - totalAntes,
                "A quantidade persistida de agendamentos ficou abaixo do volume esperado.");
        assertTrue(metricas.totalElapsedMs() <= MAX_TEMPO_TOTAL_CARGA_MS,
                () -> "Carga acima do SLA: " + metricas.totalElapsedMs() + " ms.");
        assertTrue(metricas.p95Ms() <= MAX_P95_POST_MS,
                () -> "Latência p95 acima do SLA: " + metricas.p95Ms() + " ms.");
    }

    @Test
    @Timeout(45)
    @DisplayName("Deve manter a leitura da agenda rápida com alto volume de registros")
    void deveManterLeituraDaAgendaRapidaComAltoVolumeDeRegistros() {
        TestFixture fixture = carregarFixture();
        BatchMetrics cargaPreparacao = executarCargaSimultanea(
                fixture,
                CARGA_VOLUME_ALTO,
                DIAS_OFFSET_VOLUME_ALTO);

        List<String> falhasPreparacao = cargaPreparacao.requestMetrics().stream()
                .filter(metric -> metric.statusCode() != 201)
                .map(metric -> metric.statusCode() + ": " + metric.body())
                .toList();

        assertEquals(CARGA_VOLUME_ALTO, cargaPreparacao.sucessos(),
                () -> "A massa de dados para o teste de leitura não foi criada integralmente. Falhas: "
                        + falhasPreparacao);

        List<Long> latencias = new ArrayList<>();

        for (int indice = 0; indice < REPETICOES_LEITURA; indice++) {
            long inicio = System.nanoTime();
            HttpResponse<String> response = executarGetAgendamentos(fixture.estabelecimentoId());
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
            latencias.add(elapsedMs);

            assertEquals(200, response.statusCode(), "A consulta paginada da agenda deve responder com HTTP 200.");
            assertTrue(response.body().contains("\"content\""),
                    "A resposta paginada deve conter o campo content.");
        }

        long mediaMs = Math.round(latencias.stream().mapToLong(Long::longValue).average().orElse(0));
        long piorCasoMs = latencias.stream().mapToLong(Long::longValue).max().orElse(0);

        assertTrue(mediaMs <= MAX_MEDIA_GET_MS,
                () -> "Tempo médio de leitura acima do SLA: " + mediaMs + " ms.");
        assertTrue(piorCasoMs <= MAX_PIOR_GET_MS,
                () -> "Pior tempo de leitura acima do SLA: " + piorCasoMs + " ms.");
    }

    private BatchMetrics executarCargaSimultanea(TestFixture fixture, int totalRequisicoes, int diasOffset) {
        validarCapacidadeDeSlots(fixture, totalRequisicoes);

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(totalRequisicoes, 12));
        CountDownLatch startSignal = new CountDownLatch(1);

        try {
            List<CompletableFuture<RequestMetric>> futures = IntStream.range(0, totalRequisicoes)
                    .mapToObj(indice -> CompletableFuture.supplyAsync(
                            () -> executarPostAgendamento(fixture, indice, diasOffset, startSignal),
                            executor))
                    .toList();

            long inicioLote = System.nanoTime();
            startSignal.countDown();

            List<RequestMetric> requestMetrics = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            long totalElapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicioLote);
            List<Long> latencias = requestMetrics.stream()
                    .map(RequestMetric::elapsedMs)
                    .sorted()
                    .toList();
            long p95Ms = calcularPercentil(latencias, 0.95);
            long sucessos = requestMetrics.stream()
                    .filter(metric -> metric.statusCode() == 201)
                    .count();

            return new BatchMetrics(totalElapsedMs, p95Ms, sucessos, requestMetrics);
        } finally {
            executor.shutdown();
            try {
                assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS),
                        "As threads do teste de carga não finalizaram a tempo.");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Execução do teste de carga foi interrompida.", ex);
            }
        }
    }

    private RequestMetric executarPostAgendamento(TestFixture fixture,
                                                  int indice,
                                                  int diasOffset,
                                                  CountDownLatch startSignal) {
        aguardarSinal(startSignal);

        AgendamentoPayload payload = criarPayload(fixture, indice, diasOffset);
        String body = escreverJson(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/estabelecimentos/" + fixture.estabelecimentoId() + "/agendamentos"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        long inicio = System.nanoTime();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
            return new RequestMetric(response.statusCode(), elapsedMs, response.body());
        } catch (IOException ex) {
            throw new IllegalStateException("Falha de I/O ao executar o POST de carga.", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Execução do POST de carga foi interrompida.", ex);
        }
    }

    private HttpResponse<String> executarGetAgendamentos(Long estabelecimentoId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl()
                        + "/estabelecimentos/"
                        + estabelecimentoId
                        + "/agendamentos?page=0&size="
                        + TAMANHO_PAGINA))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            throw new IllegalStateException("Falha de I/O ao executar o GET de performance.", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Execução do GET de performance foi interrompida.", ex);
        }
    }

    private TestFixture carregarFixture() {
        Long estabelecimentoId = estabelecimentoRepositoryJpa.findAll().stream()
                .sorted(Comparator.comparing(item -> item.getId()))
                .map(item -> item.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhum estabelecimento foi carregado para o teste."));

        Long servicoId = servicoRepositoryJpa.findAllByEstabelecimento_Id(estabelecimentoId).stream()
                .sorted(Comparator.comparing(item -> item.getId()))
                .map(item -> item.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhum serviço foi carregado para o teste."));

        List<Long> profissionalIds = profissionalRepositoryJpa.findAllByEstabelecimento_Id(estabelecimentoId).stream()
                .sorted(Comparator.comparing(item -> item.getId()))
                .map(item -> item.getId())
                .toList();

        List<Long> clienteIds = clienteRepositoryJpa.findAll().stream()
                .sorted(Comparator.comparing(item -> item.getId()))
                .map(item -> item.getId())
                .toList();

        assertTrue(!profissionalIds.isEmpty(),
                "É necessário ao menos 1 profissional para os testes não funcionais.");
        assertTrue(!clienteIds.isEmpty(), "É necessário ao menos 1 cliente para os testes não funcionais.");

        return new TestFixture(estabelecimentoId, servicoId, profissionalIds, clienteIds);
    }

    private void validarCapacidadeDeSlots(TestFixture fixture, int totalRequisicoes) {
        assertTrue(!fixture.profissionalIds().isEmpty(),
                "A massa de teste precisa conter ao menos um profissional disponível.");
        assertTrue(totalRequisicoes > 0,
                "O volume de requisições do teste deve ser maior que zero.");
    }

    private AgendamentoPayload criarPayload(TestFixture fixture, int indice, int diasOffset) {
        int profissionaisPorSlot = fixture.profissionalIds().size();
        int indiceSlot = indice / profissionaisPorSlot;
        int indiceDia = indiceSlot / HORARIOS_DISPONIVEIS.size();
        int hora = HORARIOS_DISPONIVEIS.get(indiceSlot % HORARIOS_DISPONIVEIS.size());

        Long profissionalId = fixture.profissionalIds().get(indice % profissionaisPorSlot);
        Long clienteId = fixture.clienteIds().get(indice % fixture.clienteIds().size());

        LocalDate dataBase = LocalDate.now()
                .plusDays(14L + diasOffset)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        LocalDate dataAgendamento = dataBase.plusWeeks(indiceDia / 5).plusDays(indiceDia % 5L);
        LocalDateTime dataHoraInicio = dataAgendamento.atTime(hora, 0);

        return new AgendamentoPayload(profissionalId, fixture.servicoId(), clienteId, dataHoraInicio);
    }

    private long calcularPercentil(List<Long> valoresOrdenados, double percentil) {
        if (valoresOrdenados.isEmpty()) {
            return 0;
        }

        int indice = Math.max(0, (int) Math.ceil(percentil * valoresOrdenados.size()) - 1);
        return valoresOrdenados.get(indice);
    }

    private String escreverJson(AgendamentoPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (IOException ex) {
            throw new IllegalStateException("Não foi possível serializar o payload do agendamento.", ex);
        }
    }

    private void aguardarSinal(CountDownLatch startSignal) {
        try {
            startSignal.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Execução do teste foi interrompida antes do disparo da carga.", ex);
        }
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private record TestFixture(Long estabelecimentoId,
                               Long servicoId,
                               List<Long> profissionalIds,
                               List<Long> clienteIds) {
    }

    private record AgendamentoPayload(Long profissionalId,
                                      Long servicoId,
                                      Long clienteId,
                                      LocalDateTime dataHoraInicio) {
    }

    private record RequestMetric(int statusCode, long elapsedMs, String body) {
        RequestMetric(int statusCode, long elapsedMs) {
            this(statusCode, elapsedMs, "");
        }
    }

    private record BatchMetrics(long totalElapsedMs,
                                long p95Ms,
                                long sucessos,
                                List<RequestMetric> requestMetrics) {

        @Override
        public String toString() {
            String statusCodes = requestMetrics.stream()
                    .map(metric -> Integer.toString(metric.statusCode()))
                    .collect(Collectors.joining(", "));
            return "BatchMetrics{"
                    + "totalElapsedMs=" + totalElapsedMs
                    + ", p95Ms=" + p95Ms
                    + ", sucessos=" + sucessos
                    + ", statusCodes=[" + statusCodes + "]"
                    + '}';
        }
    }
}
