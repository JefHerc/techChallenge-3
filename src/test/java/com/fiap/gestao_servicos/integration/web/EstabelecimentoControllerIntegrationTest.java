package com.fiap.gestao_servicos.integration.web;

import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.CreateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.DeleteEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindAllEstabelecimentosUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentosByCriteriaUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.GlobalExceptionHandler;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoController;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EstabelecimentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, EstabelecimentoControllerIntegrationTest.TestConfig.class})
class EstabelecimentoControllerIntegrationTest extends WebLayerIntegrationTestBase {

    @Autowired
    private CreateEstabelecimentoUseCase createEstabelecimentoUseCase;

    @Autowired
    private FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;

    @Autowired
    private UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase;

    @Autowired
    private DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase;

    @Test
    @DisplayName("POST /estabelecimentos deve retornar 201 e payload de estabelecimento")
    void deveCriarEstabelecimentoComSucesso() throws Exception {
        String request = """
                {
                  "nome": "Studio Beleza Centro",
                  "endereco": {
                    "logradouro": "Rua das Flores",
                    "numero": "123",
                    "bairro": "Centro",
                    "cidade": "Sao Paulo",
                    "estado": "SP",
                    "cep": "01001000"
                  },
                  "cnpj": "11.444.777/0001-61",
                  "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"],
                  "horarioFuncionamento": []
                }
                """;

        Estabelecimento estabelecimentoCriado = new Estabelecimento(
                1L,
                "Studio Beleza Centro",
                new Endereco("Rua das Flores", "123", null, "Centro", "Sao Paulo", "SP", "01001000"),
                List.of(),
                List.of(),
                new Cnpj("11.444.777/0001-61"),
                List.of("https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"),
                List.of()
        );

        when(createEstabelecimentoUseCase.create(any(Estabelecimento.class))).thenReturn(estabelecimentoCriado);

        mockMvc.perform(post("/estabelecimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Studio Beleza Centro"))
                .andExpect(jsonPath("$.cnpj").value("11.444.777/0001-61"))
                .andExpect(jsonPath("$.endereco.cidade").value("Sao Paulo"));
    }

        @Test
        @DisplayName("POST /estabelecimentos deve retornar 400 quando payload for invalido")
        void deveRetornarBadRequestQuandoPayloadForInvalido() throws Exception {
                String request = """
                                {
                                    "nome": "A",
                                    "endereco": {
                                        "logradouro": "Rua das Flores",
                                        "numero": "123",
                                        "bairro": "Centro",
                                        "cidade": "Sao Paulo",
                                        "estado": "SP",
                                        "cep": "01001000"
                                    },
                                    "cnpj": "11.444.777/0001-61",
                                    "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"],
                                    "horarioFuncionamento": []
                                }
                                """;

                mockMvc.perform(post("/estabelecimentos")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(request))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                                .andExpect(jsonPath("$.message").value("Erro de validação na requisição."))
                                .andExpect(jsonPath("$.path").value("/estabelecimentos"))
                                .andExpect(jsonPath("$.errors[0].field").value("nome"))
                                .andExpect(jsonPath("$.errors[0].detail").value("Nome deve ter entre 2 e 120 caracteres"));
        }

                            @Test
                            @DisplayName("PUT /estabelecimentos/{id} deve retornar 200 e payload atualizado")
                            void deveAtualizarEstabelecimentoComSucesso() throws Exception {
                            String request = """
                                {
                                  "nome": "Studio Beleza Centro Premium",
                                  "endereco": {
                                    "logradouro": "Rua das Flores",
                                    "numero": "125",
                                    "bairro": "Centro",
                                    "cidade": "Sao Paulo",
                                    "estado": "SP",
                                    "cep": "01001000"
                                  },
                                  "cnpj": "11.444.777/0001-61",
                                  "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"],
                                  "horarioFuncionamento": []
                                }
                                """;

                            Estabelecimento estabelecimentoAtualizado = new Estabelecimento(
                                1L,
                                "Studio Beleza Centro Premium",
                                new Endereco("Rua das Flores", "125", null, "Centro", "Sao Paulo", "SP", "01001000"),
                                List.of(),
                                List.of(),
                                new Cnpj("11.444.777/0001-61"),
                                List.of("https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"),
                                List.of()
                            );

                            when(updateEstabelecimentoUseCase.updateDadosCadastrais(eq(1L), any(Estabelecimento.class)))
                                .thenReturn(estabelecimentoAtualizado);

                            mockMvc.perform(put("/estabelecimentos/{id}", 1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Studio Beleza Centro Premium"))
                                .andExpect(jsonPath("$.endereco.numero").value("125"));
                            }

                            @Test
                            @DisplayName("PUT /estabelecimentos/{id} deve retornar 400 quando payload for invalido")
                            void deveRetornarBadRequestNoPutQuandoPayloadForInvalido() throws Exception {
                            String request = """
                                {
                                  "nome": "A",
                                  "endereco": {
                                    "logradouro": "Rua das Flores",
                                    "numero": "123",
                                    "bairro": "Centro",
                                    "cidade": "Sao Paulo",
                                    "estado": "SP",
                                    "cep": "01001000"
                                  },
                                  "cnpj": "11.444.777/0001-61",
                                  "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"],
                                  "horarioFuncionamento": []
                                }
                                """;

                            mockMvc.perform(put("/estabelecimentos/{id}", 1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                                .andExpect(jsonPath("$.path").value("/estabelecimentos/1"))
                                .andExpect(jsonPath("$.errors[0].field").value("nome"))
                                .andExpect(jsonPath("$.errors[0].detail").value("Nome deve ter entre 2 e 120 caracteres"));
                            }

                            @Test
                            @DisplayName("DELETE /estabelecimentos/{id} deve retornar 204 quando exclusao ocorrer com sucesso")
                            void deveDeletarEstabelecimentoComSucesso() throws Exception {
                            mockMvc.perform(delete("/estabelecimentos/{id}", 1L))
                                .andExpect(status().isNoContent());
                            }

                            @Test
                            @DisplayName("DELETE /estabelecimentos/{id} deve retornar erro padronizado quando nao existe")
                            void deveRetornarNotFoundNoDeleteQuandoEstabelecimentoNaoExiste() throws Exception {
                            doThrow(new ResourceNotFoundException("Estabelecimento nao encontrado"))
                                .when(deleteEstabelecimentoUseCase).deleteById(eq(999L));

                            mockMvc.perform(delete("/estabelecimentos/{id}", 999L))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                                .andExpect(jsonPath("$.message").value("Estabelecimento nao encontrado"))
                                .andExpect(jsonPath("$.path").value("/estabelecimentos/999"));
                            }

    @Test
    @DisplayName("GET /estabelecimentos/{id} deve retornar erro padronizado quando nao existe")
    void deveRetornarNotFoundComFormatoPadrao() throws Exception {
        when(findEstabelecimentoByIdUseCase.findById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Estabelecimento nao encontrado"));

        mockMvc.perform(get("/estabelecimentos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Estabelecimento nao encontrado"))
                .andExpect(jsonPath("$.path").value("/estabelecimentos/999"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        CreateEstabelecimentoUseCase createEstabelecimentoUseCase() {
            return Mockito.mock(CreateEstabelecimentoUseCase.class);
        }

        @Bean
        FindAllEstabelecimentosUseCase findAllEstabelecimentosUseCase() {
            return Mockito.mock(FindAllEstabelecimentosUseCase.class);
        }

        @Bean
        FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase() {
            return Mockito.mock(FindEstabelecimentoByIdUseCase.class);
        }

        @Bean
        UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase() {
            return Mockito.mock(UpdateEstabelecimentoUseCase.class);
        }

        @Bean
        DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase() {
            return Mockito.mock(DeleteEstabelecimentoUseCase.class);
        }

        @Bean
        FindEstabelecimentosByCriteriaUseCase findEstabelecimentosByCriteriaUseCase() {
            return Mockito.mock(FindEstabelecimentosByCriteriaUseCase.class);
        }
    }
}
