package com.fiap.gestao_servicos.integration.web;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.cliente.CreateClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.DeleteClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindAllClientesUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindClienteByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.UpdateClienteUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.GlobalExceptionHandler;
import com.fiap.gestao_servicos.infrastructure.controller.cliente.ClienteController;
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

@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, ClienteControllerIntegrationTest.TestConfig.class})
class ClienteControllerIntegrationTest extends WebLayerIntegrationTestBase {

    @Autowired
    private CreateClienteUseCase createClienteUseCase;

    @Autowired
    private FindClienteByIdUseCase findClienteByIdUseCase;

    @Autowired
    private UpdateClienteUseCase updateClienteUseCase;

    @Autowired
    private DeleteClienteUseCase deleteClienteUseCase;

    @Test
    @DisplayName("POST /clientes deve retornar 201 e payload de cliente")
    void deveCriarClienteComSucesso() throws Exception {
                String request = """
                                {
                                    "nome": "Maria da Silva",
                                    "cpf": "52998224725",
                                    "celular": "11987654321",
                                    "email": "maria.silva@email.com",
                                    "sexo": "FEMININO"
                                }
                                """;

        Cliente clienteCriado = new Cliente(
                1L,
                "Maria da Silva",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("maria.silva@email.com"),
                Sexo.FEMININO
        );

        when(createClienteUseCase.create(any(Cliente.class))).thenReturn(clienteCriado);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria da Silva"))
                .andExpect(jsonPath("$.cpf").value("52998224725"))
                .andExpect(jsonPath("$.email").value("maria.silva@email.com"));
    }

        @Test
        @DisplayName("POST /clientes deve retornar 400 quando payload for invalido")
        void deveRetornarBadRequestQuandoPayloadForInvalido() throws Exception {
                String request = """
                                {
                                    "nome": "Maria da Silva",
                                    "cpf": "52998224725",
                                    "celular": "11987654321",
                                    "email": "email-invalido",
                                    "sexo": "FEMININO"
                                }
                                """;

                mockMvc.perform(post("/clientes")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(request))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                                .andExpect(jsonPath("$.message").value("email: Email inválido"))
                    .andExpect(jsonPath("$.path").value("/clientes"));
        }

                            @Test
                            @DisplayName("PUT /clientes/{id} deve retornar 200 e payload atualizado")
                            void deveAtualizarClienteComSucesso() throws Exception {
                            String request = """
                                {
                                  "nome": "Maria de Souza",
                                  "cpf": "52998224725",
                                  "celular": "11991234567",
                                  "email": "maria.souza@email.com",
                                  "sexo": "FEMININO"
                                }
                                """;

                            Cliente clienteAtualizado = new Cliente(
                                1L,
                                "Maria de Souza",
                                new Cpf("52998224725"),
                                new Celular("11991234567"),
                                new Email("maria.souza@email.com"),
                                Sexo.FEMININO
                            );

                            when(updateClienteUseCase.update(eq(1L), any(Cliente.class))).thenReturn(clienteAtualizado);

                            mockMvc.perform(put("/clientes/{id}", 1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Maria de Souza"))
                                .andExpect(jsonPath("$.celular").value("11991234567"))
                                .andExpect(jsonPath("$.email").value("maria.souza@email.com"));
                            }

                            @Test
                            @DisplayName("PUT /clientes/{id} deve retornar 400 quando payload for invalido")
                            void deveRetornarBadRequestNoPutQuandoPayloadForInvalido() throws Exception {
                            String request = """
                                {
                                  "nome": "Maria de Souza",
                                  "cpf": "52998224725",
                                  "celular": "11991234567",
                                  "email": "email-invalido",
                                  "sexo": "FEMININO"
                                }
                                """;

                            mockMvc.perform(put("/clientes/{id}", 1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                                .andExpect(jsonPath("$.path").value("/clientes/1"));
                            }

                            @Test
                            @DisplayName("DELETE /clientes/{id} deve retornar 204 quando exclusao ocorrer com sucesso")
                            void deveDeletarClienteComSucesso() throws Exception {
                            mockMvc.perform(delete("/clientes/{id}", 1L))
                                .andExpect(status().isNoContent());
                            }

                            @Test
                            @DisplayName("DELETE /clientes/{id} deve retornar erro padronizado quando cliente nao existe")
                            void deveRetornarNotFoundNoDeleteQuandoClienteNaoExiste() throws Exception {
                            doThrow(new ResourceNotFoundException("Cliente não encontrado"))
                                .when(deleteClienteUseCase).deleteById(eq(999L));

                            mockMvc.perform(delete("/clientes/{id}", 999L))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                                .andExpect(jsonPath("$.message").value("Cliente não encontrado"))
                                .andExpect(jsonPath("$.path").value("/clientes/999"));
                            }

    @Test
    @DisplayName("GET /clientes/{id} deve retornar erro padronizado quando cliente nao existe")
    void deveRetornarNotFoundComFormatoPadrao() throws Exception {
        when(findClienteByIdUseCase.findById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado"))
                .andExpect(jsonPath("$.path").value("/clientes/999"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        CreateClienteUseCase createClienteUseCase() {
            return Mockito.mock(CreateClienteUseCase.class);
        }

        @Bean
        FindAllClientesUseCase findAllClientesUseCase() {
            return Mockito.mock(FindAllClientesUseCase.class);
        }

        @Bean
        FindClienteByIdUseCase findClienteByIdUseCase() {
            return Mockito.mock(FindClienteByIdUseCase.class);
        }

        @Bean
        UpdateClienteUseCase updateClienteUseCase() {
            return Mockito.mock(UpdateClienteUseCase.class);
        }

        @Bean
        DeleteClienteUseCase deleteClienteUseCase() {
            return Mockito.mock(DeleteClienteUseCase.class);
        }
    }
}
