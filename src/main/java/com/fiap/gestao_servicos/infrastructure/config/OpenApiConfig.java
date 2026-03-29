package com.fiap.gestao_servicos.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestaoServicosOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addExamples("PageResultExample", new Example().value("""
                    {
                      "content": [
                        {
                          "id": 1,
                          "nome": "Exemplo"
                        }
                      ],
                      "page": {
                        "size": 20,
                        "number": 0,
                        "totalElements": 1,
                        "totalPages": 1
                      }
                    }
                    """))
                .addResponses("BadRequestError", new ApiResponse()
                    .description("Dados invalidos")
                    .content(new Content().addMediaType("application/json", new MediaType()
                        .schema(new ObjectSchema())
                        .addExamples("erroValidacao", new Example().value("""
                            {
                              "status": 400,
                              "code": "VALIDATION_ERROR",
                              "message": "Dados invalidos",
                              "timestamp": "2026-03-29T12:00:00Z",
                              "path": "/recurso",
                              "errors": [
                                {
                                  "field": "campo",
                                  "message": "mensagem de validacao"
                                }
                              ]
                            }
                            """)))))
                .addResponses("NotFoundError", new ApiResponse()
                    .description("Recurso nao encontrado")
                    .content(new Content().addMediaType("application/json", new MediaType()
                        .schema(new ObjectSchema())
                        .addExamples("erroNaoEncontrado", new Example().value("""
                            {
                              "status": 404,
                              "code": "RESOURCE_NOT_FOUND",
                              "message": "Recurso nao encontrado para o id informado",
                              "timestamp": "2026-03-29T12:00:00Z",
                              "path": "/recurso/1",
                              "errors": []
                            }
                            """))))))
                .info(new Info()
                        .title("Gestao Servicos API")
                        .description("API para agendamento e gerenciamento de servicos de beleza e bem-estar")
                        .version("v1"));
    }
}
