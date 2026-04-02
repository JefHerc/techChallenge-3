package com.fiap.gestao_servicos.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenApiConfigTest {

    @Test
    void deveCriarOpenApiComSchemasResponsesEExamples() {
        OpenApiConfig config = new OpenApiConfig();

        OpenAPI openAPI = config.gestaoServicosOpenAPI();

        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getInfo());
        assertNotNull(openAPI.getComponents().getSchemas().get("ApiErrorResponse"));
        assertNotNull(openAPI.getComponents().getExamples().get("PageResultExample"));
        assertNotNull(openAPI.getComponents().getResponses().get("BadRequestError"));
        assertNotNull(openAPI.getComponents().getResponses().get("DuplicateDataError"));
        assertNotNull(openAPI.getComponents().getResponses().get("NotFoundError"));
        assertNotNull(openAPI.getComponents().getResponses().get("DataIntegrityViolationException"));
        assertNotNull(openAPI.getComponents().getResponses().get("InternalServerError"));
    }

    @Test
    void deveCustomizarResponsesEExemploPaginadoConformeMetodo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = new OpenAPI();

        Operation getById = operationWithJson200AndDefault();
        Operation getList = operationWithJson200AndDefault();
        Operation postWithPathVariable = operationWithJson200AndDefault();
        Operation deleteById = operationWithJson200AndDefault();
        Operation optionsOperation = operationWithJson200AndDefault();

        PathItem detalhe = new PathItem();
        detalhe.setGet(getById);
        detalhe.setPost(postWithPathVariable);
        detalhe.setDelete(deleteById);
        detalhe.setOptions(optionsOperation);

        PathItem lista = new PathItem();
        lista.setGet(getList);

        Paths paths = new Paths();
        paths.addPathItem("/clientes/{id}", detalhe);
        paths.addPathItem("/clientes", lista);
        openAPI.setPaths(paths);

        config.swaggerResponseCustomizer().customise(openAPI);

        assertNotNull(getById.getResponses().get("400"));
        assertNotNull(getById.getResponses().get("404"));
        assertNotNull(getById.getResponses().get("500"));
        assertNull(getById.getResponses().get("409"));
        assertNull(getById.getResponses().get("default"));

        assertNotNull(postWithPathVariable.getResponses().get("400"));
        assertNotNull(postWithPathVariable.getResponses().get("404"));
        assertNotNull(postWithPathVariable.getResponses().get("409"));
        assertNotNull(postWithPathVariable.getResponses().get("500"));

        assertNotNull(deleteById.getResponses().get("404"));
        assertNotNull(deleteById.getResponses().get("409"));
        assertNotNull(deleteById.getResponses().get("500"));

        assertNotNull(optionsOperation.getResponses().get("500"));
        assertNull(optionsOperation.getResponses().get("400"));

        MediaType listJson = getList.getResponses().get("200").getContent().get("application/json");
        assertNotNull(listJson.getSchema());
        assertNotNull(listJson.getExample());
    }

    @Test
    void deveIgnorarQuandoPathsOuPathItemOuResponsesForemNulos() {
        OpenApiConfig config = new OpenApiConfig();

        OpenAPI semPaths = new OpenAPI();
        config.swaggerResponseCustomizer().customise(semPaths);

        OpenAPI pathItemNulo = new OpenAPI();
        Paths paths = new Paths();
        paths.addPathItem("/nulo", null);
        pathItemNulo.setPaths(paths);
        config.swaggerResponseCustomizer().customise(pathItemNulo);

        OpenAPI responsesNulas = new OpenAPI();
        Operation getSemResponses = new Operation();
        PathItem pathComGet = new PathItem();
        pathComGet.setGet(getSemResponses);
        responsesNulas.setPaths(new Paths().addPathItem("/sem-responses/{id}", pathComGet));

        config.swaggerResponseCustomizer().customise(responsesNulas);

        assertNull(getSemResponses.getResponses());
        assertTrue(true);
    }

    private Operation operationWithJson200AndDefault() {
        ApiResponses responses = new ApiResponses();
        responses.addApiResponse("default", new ApiResponse().description("default"));
        responses.addApiResponse("200", new ApiResponse().content(
                new Content().addMediaType("application/json", new MediaType())
        ));

        Operation operation = new Operation();
        operation.setResponses(responses);
        return operation;
    }
}
