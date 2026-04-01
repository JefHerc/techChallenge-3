package com.fiap.gestao_servicos.integration.web;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.agendamento.CreateAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.DeleteAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAllAgendamentosUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.UpdateAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.input.AgendamentoInput;
import com.fiap.gestao_servicos.infrastructure.controller.GlobalExceptionHandler;
import com.fiap.gestao_servicos.infrastructure.controller.agendamento.AgendamentoController;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@WebMvcTest(controllers = AgendamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, AgendamentoControllerIntegrationTest.TestConfig.class})
class AgendamentoControllerIntegrationTest extends WebLayerIntegrationTestBase {

    @Autowired
    private CreateAgendamentoUseCase createAgendamentoUseCase;

    @Autowired
    private FindAgendamentoByIdUseCase findAgendamentoByIdUseCase;

    @Autowired
    private FindAllAgendamentosUseCase findAllAgendamentosUseCase;

    @Autowired
    private UpdateAgendamentoUseCase updateAgendamentoUseCase;

    @Autowired
    private DeleteAgendamentoUseCase deleteAgendamentoUseCase;

    @Test
    @DisplayName("POST /estabelecimentos/{id}/agendamentos deve retornar 201 e payload de agendamento")
    void deveCriarAgendamentoComSucesso() throws Exception {
        String request = """
                {
                  "profissionalId": 5,
                  "servicoId": 20,
                  "clienteId": 12,
                                    "dataHoraInicio": "2026-04-20T14:30:00"
                }
                """;

        Agendamento agendamentoCriado = new Agendamento(
                10L,
                new Profissional(5L, "Ana Souza", null, null, null, null, null, List.of(), Sexo.FEMININO, List.of()),
                new Servico(20L, ServicoEnum.CORTE, Duration.ofHours(1)),
                new Estabelecimento(1L, "Studio Beleza Centro", null, List.of(), List.of(), null, List.of(), List.of()),
                new Cliente(
                        12L,
                        "Maria da Silva",
                        new Cpf("52998224725"),
                        new Celular("11987654321"),
                        new Email("maria.silva@email.com"),
                        Sexo.FEMININO
                ),
                LocalDateTime.parse("2026-04-20T14:30:00"),
                AgendamentoStatus.AGENDADO
        );

        when(createAgendamentoUseCase.create(any(AgendamentoInput.class))).thenReturn(agendamentoCriado);

        mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/agendamentos", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.profissionalId").value(5))
                .andExpect(jsonPath("$.profissionalNome").value("Ana Souza"))
                .andExpect(jsonPath("$.servicoId").value(20))
                .andExpect(jsonPath("$.servicoNome").value("Corte"))
                .andExpect(jsonPath("$.estabelecimentoId").value(1))
                .andExpect(jsonPath("$.estabelecimentoNome").value("Studio Beleza Centro"))
                .andExpect(jsonPath("$.clienteId").value(12))
                .andExpect(jsonPath("$.clienteNome").value("Maria da Silva"))
                .andExpect(jsonPath("$.dataHoraInicio").value("2026-04-20T14:30:00"))
                .andExpect(jsonPath("$.dataHoraFim").value("2026-04-20T15:30:00"))
                .andExpect(jsonPath("$.status").value("AGENDADO"));
    }

    @Test
        @DisplayName("POST /estabelecimentos/{id}/agendamentos deve retornar 400 quando status for informado")
        void deveRetornarBadRequestQuandoStatusForInformadoNoPost() throws Exception {
        String request = """
                {
                  "profissionalId": 5,
                  "servicoId": 20,
                  "clienteId": 12,
                  "dataHoraInicio": "2026-04-20T14:30:00",
                                    "status": "AGENDADO"
                }
                """;

        mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/agendamentos", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                .andExpect(jsonPath("$.message").value("status: Status não deve ser informado na criação do agendamento"))
            .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos"));
    }

            @Test
            @DisplayName("PUT /estabelecimentos/{id}/agendamentos/{agendamentoId} deve retornar 200 e payload atualizado")
            void deveAtualizarAgendamentoComSucesso() throws Exception {
            String request = """
                {
                  "profissionalId": 5,
                  "servicoId": 20,
                  "clienteId": 12,
                  "dataHoraInicio": "2026-04-20T15:00:00",
                  "status": "CONCLUIDO"
                }
                """;

            Agendamento atual = novoAgendamento(10L, LocalDateTime.parse("2026-04-20T14:30:00"), AgendamentoStatus.AGENDADO);
            Agendamento agendamentoAtualizado = novoAgendamento(10L, LocalDateTime.parse("2026-04-20T15:00:00"), AgendamentoStatus.CONCLUIDO);

            when(findAgendamentoByIdUseCase.findById(eq(10L))).thenReturn(atual);
            when(updateAgendamentoUseCase.update(eq(10L), any(AgendamentoInput.class))).thenReturn(agendamentoAtualizado);

            mockMvc.perform(put("/estabelecimentos/{estabelecimentoId}/agendamentos/{id}", 1L, 10L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.dataHoraInicio").value("2026-04-20T15:00:00"))
                .andExpect(jsonPath("$.dataHoraFim").value("2026-04-20T16:00:00"))
                .andExpect(jsonPath("$.status").value("CONCLUIDO"));
            }

            @Test
            @DisplayName("PUT /estabelecimentos/{id}/agendamentos/{agendamentoId} deve retornar 400 quando payload for invalido")
            void deveRetornarBadRequestNoPutQuandoPayloadForInvalido() throws Exception {
            String request = """
                {
                  "profissionalId": 5,
                  "servicoId": 20,
                  "clienteId": 12,
                  "dataHoraInicio": "2026-04-20T15:00:00",
                  "status": ""
                }
                """;

            mockMvc.perform(put("/estabelecimentos/{estabelecimentoId}/agendamentos/{id}", 1L, 10L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/10"));
            }

            @Test
            @DisplayName("DELETE /estabelecimentos/{id}/agendamentos/{agendamentoId} deve retornar 204 quando exclusao ocorrer com sucesso")
            void deveDeletarAgendamentoComSucesso() throws Exception {
            when(findAgendamentoByIdUseCase.findById(eq(10L)))
                .thenReturn(novoAgendamento(10L, LocalDateTime.parse("2026-04-20T14:30:00"), AgendamentoStatus.AGENDADO));

            mockMvc.perform(delete("/estabelecimentos/{estabelecimentoId}/agendamentos/{id}", 1L, 10L))
                .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("DELETE /estabelecimentos/{id}/agendamentos/{agendamentoId} deve retornar erro padronizado quando nao existe")
            void deveRetornarNotFoundNoDeleteQuandoAgendamentoNaoExiste() throws Exception {
            when(findAgendamentoByIdUseCase.findById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Agendamento não encontrado"));
            doThrow(new ResourceNotFoundException("Agendamento não encontrado"))
                .when(deleteAgendamentoUseCase).deleteById(eq(999L));

            mockMvc.perform(delete("/estabelecimentos/{estabelecimentoId}/agendamentos/{id}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Agendamento não encontrado"))
                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/999"));
            }

    @Test
    @DisplayName("GET /estabelecimentos/{id}/agendamentos/periodo deve retornar agendamentos do intervalo informado")
    void deveListarAgendamentosPorPeriodo() throws Exception {
        Agendamento primeiro = novoAgendamento(10L, LocalDateTime.parse("2026-04-20T14:30:00"), AgendamentoStatus.AGENDADO);
        Agendamento segundo = novoAgendamento(11L, LocalDateTime.parse("2026-04-21T09:00:00"), AgendamentoStatus.CONCLUIDO);

        when(findAllAgendamentosUseCase.findByEstabelecimentoIdAndPeriodo(
                eq(1L),
                eq(LocalDate.of(2026, 4, 20)),
                eq(LocalDate.of(2026, 4, 21))))
                .thenReturn(List.of(primeiro, segundo));

        mockMvc.perform(get("/estabelecimentos/{estabelecimentoId}/agendamentos/periodo", 1L)
                        .param("dataInicial", "2026-04-20")
                        .param("dataFinal", "2026-04-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].status").value("AGENDADO"))
                .andExpect(jsonPath("$[1].id").value(11))
                .andExpect(jsonPath("$[1].status").value("CONCLUIDO"));
    }

    @Test
    @DisplayName("GET /estabelecimentos/{id}/agendamentos/{agendamentoId} deve retornar erro padronizado quando nao existe")
    void deveRetornarNotFoundComFormatoPadrao() throws Exception {
        when(findAgendamentoByIdUseCase.findById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Agendamento não encontrado"));

        mockMvc.perform(get("/estabelecimentos/{estabelecimentoId}/agendamentos/{id}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Agendamento não encontrado"))
                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/999"));
    }

            private Agendamento novoAgendamento(Long id, LocalDateTime dataHoraInicio, AgendamentoStatus status) {
            return new Agendamento(
                id,
                new Profissional(5L, "Ana Souza", null, null, null, null, null, List.of(), Sexo.FEMININO, List.of()),
                new Servico(20L, ServicoEnum.CORTE, Duration.ofHours(1)),
                new Estabelecimento(1L, "Studio Beleza Centro", null, List.of(), List.of(), null, List.of(), List.of()),
                new Cliente(
                    12L,
                    "Maria da Silva",
                    new Cpf("52998224725"),
                    new Celular("11987654321"),
                    new Email("maria.silva@email.com"),
                    Sexo.FEMININO
                ),
                dataHoraInicio,
                status
            );
            }

    @TestConfiguration
    static class TestConfig {

        @Bean
        CreateAgendamentoUseCase createAgendamentoUseCase() {
            return Mockito.mock(CreateAgendamentoUseCase.class);
        }

        @Bean
        FindAllAgendamentosUseCase findAllAgendamentosUseCase() {
            return Mockito.mock(FindAllAgendamentosUseCase.class);
        }

        @Bean
        FindAgendamentoByIdUseCase findAgendamentoByIdUseCase() {
            return Mockito.mock(FindAgendamentoByIdUseCase.class);
        }

        @Bean
        UpdateAgendamentoUseCase updateAgendamentoUseCase() {
            return Mockito.mock(UpdateAgendamentoUseCase.class);
        }

        @Bean
        DeleteAgendamentoUseCase deleteAgendamentoUseCase() {
            return Mockito.mock(DeleteAgendamentoUseCase.class);
        }
    }
}