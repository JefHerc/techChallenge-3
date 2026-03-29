package com.fiap.gestao_servicos.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.gestao_servicos.GestaoServicosApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class BddTestServer {

    private static final Object LOCK = new Object();
    private static final ThreadLocal<ScenarioContext> CONTEXT = ThreadLocal.withInitial(ScenarioContext::new);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private static ConfigurableApplicationContext applicationContext;
    private static String baseUrl;

    private BddTestServer() {
    }

    public static void startIfNeeded() {
        if (applicationContext != null) {
            return;
        }

        synchronized (LOCK) {
            if (applicationContext != null) {
                return;
            }

            applicationContext = new SpringApplicationBuilder(GestaoServicosApplication.class)
                    .profiles("bdd")
                    .properties(
                            "server.port=0",
                            "spring.main.banner-mode=off"
                    )
                    .run();

            Integer port = applicationContext.getEnvironment().getProperty("local.server.port", Integer.class);
            if (port == null) {
                throw new IllegalStateException("Nao foi possivel obter a porta local da aplicacao BDD.");
            }

            baseUrl = "http://127.0.0.1:" + port;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (applicationContext != null) {
                    applicationContext.close();
                }
            }));
        }
    }

    public static ScenarioContext context() {
        return CONTEXT.get();
    }

    public static void resetScenario() {
        CONTEXT.get().reset();
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    public static HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .header("Accept", "application/json")
                .GET()
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .header("Accept", "application/json")
                .DELETE()
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> post(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> put(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static URI uri(String path) {
        startIfNeeded();
        return URI.create(baseUrl + path);
    }
}