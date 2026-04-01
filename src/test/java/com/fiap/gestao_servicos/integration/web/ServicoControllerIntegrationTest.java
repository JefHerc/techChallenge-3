package com.fiap.gestao_servicos.integration.web;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicosNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.GlobalExceptionHandler;
import com.fiap.gestao_servicos.infrastructure.controller.servico.ServicoController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ServicoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, ServicoControllerIntegrationTest.TestConfig.class})
class ServicoControllerIntegrationTest extends WebLayerIntegrationTestBase {

    @Autowired
    private CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase;

    @Autowired
    private FindServicoByIdUseCase findServicoByIdUseCase;

    @Autowired
    private UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase;

    @Autowired
    private DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase;

    @Test
    @DisplayName("POST /estabelecimentos/{id}/servicos deve retornar 201 com lista de servicos")
    void deveCriarServicosComSucesso() throws Exception {
        String request = """
                [
                  {
                    "nome": "CORTE",
                    "duracaoMedia": "PT1H"
                  }
                ]
                """;

        Servico servicoCriado = new Servico(20L, ServicoEnum.CORTE, Duration.ofHours(1));

        when(createServicosNoEstabelecimentoUseCase.create(eq(1L), anyList()))
                .thenReturn(List.of(servicoCriado));

        mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/servicos", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].nome").value("Corte"))
                .andExpect(jsonPath("$[0].duracaoMedia").value("PT1H"));
    }

        @Test
        @DisplayName("POST /estabelecimentos/{id}/servicos deve retornar 400 quando payload for invalido")
        void deveRetornarBadRequestQuandoPayloadForInvalido() throws Exception {
                String request = """
                                [
                                    {
                                        "nome": "",
                                        "duracaoMedia": "PT1H"
                                    }
                                ]
                                """;

                mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/servicos", 1L)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(request))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                                .andExpect(jsonPath("$.message").value("Erro de validação na requisição."))
                                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/servicos"));
        }

                            @Test
                            @DisplayName("PUT /estabelecimentos/{id}/servicos/{servicoId} deve retornar 200 com servico atualizado")
                            void deveAtualizarServicoComSucesso() throws Exception {
                            String request = """
                                {
                                  "nome": "CORTE",
                                  "duracaoMedia": "PT1H15M"
                                }
                                """;

                            Servico servicoAtualizado = new Servico(20L, ServicoEnum.CORTE, Duration.ofMinutes(75));

                            when(updateServicoNoEstabelecimentoUseCase.update(eq(1L), eq(20L), org.mockito.ArgumentMatchers.any(Servico.class)))
                                .thenReturn(servicoAtualizado);

                            mockMvc.perform(put("/estabelecimentos/{estabelecimentoId}/servicos/{id}", 1L, 20L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(20))
                                .andExpect(jsonPath("$.nome").value("Corte"))
                                .andExpect(jsonPath("$.duracaoMedia").value("PT1H15M"));
                            }

                            @Test
                            @DisplayName("PUT /estabelecimentos/{id}/servicos/{servicoId} deve retornar 400 quando payload for invalido")
                            void deveRetornarBadRequestNoPutQuandoPayloadForInvalido() throws Exception {
                            String request = """
                                {
                                  "nome": "A",
                                  "duracaoMedia": "PT1H15M"
                                }
                                """;

                            mockMvc.perform(put("/estabelecimentos/{estabelecimentoId}/servicos/{id}", 1L, 20L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/servicos/20"));
                            }

                            @Test
                            @DisplayName("DELETE /estabelecimentos/{id}/servicos/{servicoId} deve retornar 204 quando exclusao ocorrer com sucesso")
                            void deveDeletarServicoComSucesso() throws Exception {
                            mockMvc.perform(delete("/estabelecimentos/{estabelecimentoId}/servicos/{id}", 1L, 20L))
                                .andExpect(status().isNoContent());
                            }

                            @Test
                            @DisplayName("DELETE /estabelecimentos/{id}/servicos/{servicoId} deve retornar erro padronizado quando nao existe")
                            void deveRetornarNotFoundNoDeleteQuandoServicoNaoExiste() throws Exception {
                            doThrow(new ResourceNotFoundException("Servico nao encontrado para o id: 999 no estabelecimento: 1"))
                                .when(deleteServicoNoEstabelecimentoUseCase).deleteById(eq(1L), eq(999L));

                            mockMvc.perform(delete("/estabelecimentos/{estabelecimentoId}/servicos/{id}", 1L, 999L))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                                .andExpect(jsonPath("$.message").value("Servico nao encontrado para o id: 999 no estabelecimento: 1"))
                                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/servicos/999"));
                            }

    @Test
    @DisplayName("GET /estabelecimentos/{id}/servicos/{servicoId} deve retornar erro padronizado quando nao existe")
    void deveRetornarNotFoundComFormatoPadrao() throws Exception {
        when(findServicoByIdUseCase.findByIdAndEstabelecimentoId(eq(999L), eq(1L)))
                .thenThrow(new ResourceNotFoundException("Servico nao encontrado para o id: 999 no estabelecimento: 1"));

        mockMvc.perform(get("/estabelecimentos/{estabelecimentoId}/servicos/{id}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Servico nao encontrado para o id: 999 no estabelecimento: 1"))
                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/servicos/999"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase() {
            return Mockito.mock(CreateServicosNoEstabelecimentoUseCase.class);
        }

        @Bean
        FindAllServicosUseCase findAllServicosUseCase() {
            return Mockito.mock(FindAllServicosUseCase.class);
        }

        @Bean
        FindServicoByIdUseCase findServicoByIdUseCase() {
            return Mockito.mock(FindServicoByIdUseCase.class);
        }

        @Bean
        UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase() {
            return Mockito.mock(UpdateServicoNoEstabelecimentoUseCase.class);
        }

        @Bean
        DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase() {
            return Mockito.mock(DeleteServicoNoEstabelecimentoUseCase.class);
        }
    }
}
