package com.fiap.gestao_servicos.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestaoServicosOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addExamples("PageResultExample", new Example().value(pageResultExample()))
                        .addSchemas("ApiErrorResponse", apiErrorSchema())
                        .addResponses("BadRequestError", buildResponseComponent(
                                "Dados inválidos",
                                "erroValidacao",
                                400,
                                "VALIDACAO_ENTRADA",
                                "Erro de validação na requisição.",
                                "/recurso"
                        ))
                        .addResponses("DuplicateDataError", buildResponseComponent(
                                "Conflito por dado duplicado",
                                "erroDadoDuplicado",
                                409,
                                "DADO_DUPLICADO",
                                "Já existe um recurso com os mesmos dados únicos.",
                                "/recurso"
                        ))
                        .addResponses("NotFoundError", buildResponseComponent(
                                "Recurso não encontrado",
                                "erroNaoEncontrado",
                                404,
                                "NAO_ENCONTRADO",
                                "Recurso não encontrado para o id informado",
                                "/recurso/1"
                        ))
                        .addResponses("DataIntegrityViolationException", buildResponseComponent(
                                "Violação de integridade de dados",
                                "erroIntegridadeDados",
                                409,
                                "RECURSO_EM_USO",
                                "Não é possível concluir a operação porque existem dados vinculados a este recurso.",
                                "/recurso/1"
                        ))
                        .addResponses("InternalServerError", buildResponseComponent(
                                "Erro interno no servidor",
                                "erroInterno",
                                500,
                                "ERRO_INTERNO",
                                "Erro interno no servidor.",
                                "/recurso"
                        )))
                .info(new Info()
                        .title("Gestao Servicos API")
                        .description("API para agendamento e gerenciamento de servicos de beleza e bem-estar")
                        .version("v1"));
    }

    @Bean
    public GlobalOpenApiCustomizer swaggerResponseCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) -> {
                if (pathItem == null) {
                    return;
                }

                pathItem.readOperationsMap().forEach((method, operation) -> {
                    normalizeErrorResponses(path, method, operation);
                    applyPagedSuccessExample(path, method, operation);
                });
            });
        };
    }

    private static ApiResponse buildResponseComponent(String description,
                                                      String exampleName,
                                                      int status,
                                                      String code,
                                                      String message,
                                                      String path) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json", new MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse"))
                        .addExamples(exampleName, new Example().value(errorExample(status, code, message, path)))));
    }

    private static Schema<?> apiErrorSchema() {
        return new ObjectSchema()
                .addProperty("status", new Schema<Integer>().type("integer").example(400))
                .addProperty("code", new Schema<String>().type("string").example("VALIDACAO_ENTRADA"))
                .addProperty("message", new Schema<String>().type("string").example("Erro de validação na requisição."))
                .addProperty("timestamp", new Schema<String>().type("string").format("date-time").example("2026-03-29T12:00:00Z"))
                .addProperty("path", new Schema<String>().type("string").example("/recurso"));
    }

    private static void normalizeErrorResponses(String path, PathItem.HttpMethod method, Operation operation) {
        if (operation.getResponses() == null) {
            return;
        }

        operation.getResponses().remove("default");
        addResponseIfAbsent(operation, "500", "#/components/responses/InternalServerError");

        switch (method) {
            case GET -> {
                addResponseIfAbsent(operation, "400", "#/components/responses/BadRequestError");
                if (path.contains("{")) {
                    addResponseIfAbsent(operation, "404", "#/components/responses/NotFoundError");
                }
            }
            case POST, PUT, PATCH -> {
                addResponseIfAbsent(operation, "400", "#/components/responses/BadRequestError");
                if (path.contains("{")) {
                    addResponseIfAbsent(operation, "404", "#/components/responses/NotFoundError");
                }
                addResponseIfAbsent(operation, "409", "#/components/responses/DuplicateDataError");
            }
            case DELETE -> {
                addResponseIfAbsent(operation, "404", "#/components/responses/NotFoundError");
                addResponseIfAbsent(operation, "409", "#/components/responses/DataIntegrityViolationException");
            }
            default -> {
            }
        }
    }

    private static void addResponseIfAbsent(Operation operation, String responseCode, String responseRef) {
        if (!operation.getResponses().containsKey(responseCode)) {
            operation.getResponses().addApiResponse(responseCode, new ApiResponse().$ref(responseRef));
        }
    }

    private static void applyPagedSuccessExample(String path, PathItem.HttpMethod method, Operation operation) {
        if (method != PathItem.HttpMethod.GET || path.contains("{")) {
            return;
        }

        ApiResponse successResponse = operation.getResponses().get("200");
        if (successResponse == null || successResponse.getContent() == null) {
            return;
        }

        MediaType jsonContent = successResponse.getContent().get("application/json");
        if (jsonContent == null) {
            return;
        }

        if (jsonContent.getSchema() == null) {
            jsonContent.setSchema(new ObjectSchema());
        }

        jsonContent.setExample(pageResultExample());
    }

    private static Map<String, Object> errorExample(int status, String code, String message, String path) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", status);
        error.put("code", code);
        error.put("message", message);
        error.put("timestamp", "2026-03-29T12:00:00Z");
        error.put("path", path);
        return error;
    }

    private static Map<String, Object> pageResultExample() {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", 1);
        item.put("nome", "Exemplo");

        Map<String, Object> page = new LinkedHashMap<>();
        page.put("size", 20);
        page.put("number", 0);
        page.put("totalElements", 1);
        page.put("totalPages", 1);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("content", List.of(item));
        result.put("page", page);
        return result;
    }
}
