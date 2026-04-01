package com.fiap.gestao_servicos.integration.web;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
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
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.CreateAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.DeleteAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAllAvaliacoesUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAvaliacaoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.UpdateAvaliacaoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.GlobalExceptionHandler;
import com.fiap.gestao_servicos.infrastructure.controller.avaliacao.AvaliacaoController;
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

@WebMvcTest(controllers = AvaliacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, AvaliacaoControllerIntegrationTest.TestConfig.class})
class AvaliacaoControllerIntegrationTest extends WebLayerIntegrationTestBase {

    @Autowired
    private FindAgendamentoByIdUseCase findAgendamentoByIdUseCase;

    @Autowired
    private CreateAvaliacaoUseCase createAvaliacaoUseCase;

    @Autowired
    private FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase;

    @Autowired
    private UpdateAvaliacaoUseCase updateAvaliacaoUseCase;

    @Autowired
    private DeleteAvaliacaoUseCase deleteAvaliacaoUseCase;

    @Test
        @DisplayName("POST /estabelecimentos/{id}/agendamentos/{agendamentoId}/avaliacoes deve retornar 201 e payload de avaliacao")
    void deveCriarAvaliacaoComSucesso() throws Exception {
        String request = """
                {
                                    "agendamentoId": 10,
                  "notaEstabelecimento": 4.5,
                  "notaProfissional": 5.0,
                  "comentarioEstabelecimento": "Ambiente limpo e atendimento excelente",
                  "comentarioProfissional": "Profissional muito atenciosa"
                }
                """;

        Agendamento agendamento = novoAgendamento(10L);
        Avaliacao avaliacaoCriada = new Avaliacao(
                30L,
                agendamento,
                4.5,
                5.0,
                "Ambiente limpo e atendimento excelente",
                "Profissional muito atenciosa"
        );

        when(findAgendamentoByIdUseCase.findById(eq(10L))).thenReturn(agendamento);
        when(createAvaliacaoUseCase.create(any(Avaliacao.class))).thenReturn(avaliacaoCriada);

        mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/agendamentos/{agendamentoId}/avaliacoes", 1L, 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(30))
                .andExpect(jsonPath("$.agendamentoId").value(10))
                .andExpect(jsonPath("$.profissionalId").value(5))
                .andExpect(jsonPath("$.profissionalNome").value("Ana Souza"))
                .andExpect(jsonPath("$.estabelecimentoId").value(1))
                .andExpect(jsonPath("$.estabelecimentoNome").value("Studio Beleza Centro"))
                .andExpect(jsonPath("$.statusAgendamento").value("AGENDADO"))
                .andExpect(jsonPath("$.notaEstabelecimento").value(4.5))
                .andExpect(jsonPath("$.notaProfissional").value(5.0))
                .andExpect(jsonPath("$.comentarioEstabelecimento").value("Ambiente limpo e atendimento excelente"))
                .andExpect(jsonPath("$.comentarioProfissional").value("Profissional muito atenciosa"));
    }

    @Test
        @DisplayName("POST /estabelecimentos/{id}/agendamentos/{agendamentoId}/avaliacoes deve retornar 400 quando payload for invalido")
    void deveRetornarBadRequestQuandoPayloadForInvalido() throws Exception {
        String request = """
                {
                                    "agendamentoId": 10,
                  "notaEstabelecimento": 4.5,
                  "notaProfissional": 5.5,
                  "comentarioEstabelecimento": "Ambiente limpo e atendimento excelente",
                  "comentarioProfissional": "Profissional muito atenciosa"
                }
                """;

        mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/agendamentos/{agendamentoId}/avaliacoes", 1L, 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                .andExpect(jsonPath("$.message").value("notaProfissional: Nota do profissional deve ser entre 0 e 5"))
                    .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/10/avaliacoes"));
    }

            @Test
            @DisplayName("PUT /estabelecimentos/{id}/agendamentos/{agendamentoId}/avaliacoes/{avaliacaoId} deve retornar 200 e payload atualizado")
            void deveAtualizarAvaliacaoComSucesso() throws Exception {
            String request = """
                {
                  "notaEstabelecimento": 4.0,
                  "notaProfissional": 4.8,
                  "comentarioEstabelecimento": "Servico bom e rapido",
                  "comentarioProfissional": "Excelente tecnica"
                }
                """;

            Agendamento agendamento = novoAgendamento(10L);
            Avaliacao avaliacaoAtual = new Avaliacao(
                30L,
                agendamento,
                4.5,
                5.0,
                "Ambiente limpo e atendimento excelente",
                "Profissional muito atenciosa"
            );
            Avaliacao avaliacaoAtualizada = new Avaliacao(
                30L,
                agendamento,
                4.0,
                4.8,
                "Servico bom e rapido",
                "Excelente tecnica"
            );

            when(findAvaliacaoByIdUseCase.findById(eq(30L))).thenReturn(avaliacaoAtual);
            when(updateAvaliacaoUseCase.update(eq(30L), any(Avaliacao.class))).thenReturn(avaliacaoAtualizada);

                mockMvc.perform(put("/estabelecimentos/{estabelecimentoId}/agendamentos/{agendamentoId}/avaliacoes/{id}", 1L, 10L, 30L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30))
                .andExpect(jsonPath("$.agendamentoId").value(10))
                .andExpect(jsonPath("$.notaEstabelecimento").value(4.0))
                .andExpect(jsonPath("$.notaProfissional").value(4.8))
                .andExpect(jsonPath("$.comentarioEstabelecimento").value("Servico bom e rapido"))
                .andExpect(jsonPath("$.comentarioProfissional").value("Excelente tecnica"));
            }

            @Test
            @DisplayName("PUT /estabelecimentos/{id}/agendamentos/{agendamentoId}/avaliacoes/{avaliacaoId} deve retornar 400 quando payload for invalido")
            void deveRetornarBadRequestNoPutQuandoPayloadForInvalido() throws Exception {
            String request = """
                {
                  "notaEstabelecimento": 4.0,
                  "notaProfissional": 5.5,
                  "comentarioEstabelecimento": "Servico bom e rapido",
                  "comentarioProfissional": "Excelente tecnica"
                }
                """;

                mockMvc.perform(put("/estabelecimentos/{estabelecimentoId}/agendamentos/{agendamentoId}/avaliacoes/{id}", 1L, 10L, 30L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDACAO_ENTRADA"))
                    .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/10/avaliacoes/30"));
            }

            @Test
            @DisplayName("DELETE /estabelecimentos/{id}/agendamentos/avaliacoes/{avaliacaoId} deve retornar 204 quando exclusao ocorrer com sucesso")
            void deveDeletarAvaliacaoComSucesso() throws Exception {
            Avaliacao avaliacao = new Avaliacao(
                30L,
                novoAgendamento(10L),
                4.5,
                5.0,
                "Ambiente limpo e atendimento excelente",
                "Profissional muito atenciosa"
            );

            when(findAvaliacaoByIdUseCase.findById(eq(30L))).thenReturn(avaliacao);

            mockMvc.perform(delete("/estabelecimentos/{estabelecimentoId}/agendamentos/avaliacoes/{id}", 1L, 30L))
                .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("DELETE /estabelecimentos/{id}/agendamentos/avaliacoes/{avaliacaoId} deve retornar erro padronizado quando nao existe")
            void deveRetornarNotFoundNoDeleteQuandoAvaliacaoNaoExiste() throws Exception {
            when(findAvaliacaoByIdUseCase.findById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Avaliação não encontrada"));
            doThrow(new ResourceNotFoundException("Avaliação não encontrada"))
                .when(deleteAvaliacaoUseCase).deleteById(eq(999L));

            mockMvc.perform(delete("/estabelecimentos/{estabelecimentoId}/agendamentos/avaliacoes/{id}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Avaliação não encontrada"))
                .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/avaliacoes/999"));
            }

    @Test
    @DisplayName("GET /estabelecimentos/{id}/agendamentos/{agendamentoId}/avaliacoes/{avaliacaoId} deve retornar erro padronizado quando nao existe")
    void deveRetornarNotFoundComFormatoPadrao() throws Exception {
        when(findAvaliacaoByIdUseCase.findById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Avaliação não encontrada"));

        mockMvc.perform(get("/estabelecimentos/{estabelecimentoId}/agendamentos/{agendamentoId}/avaliacoes/{id}", 1L, 10L, 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.message").value("Avaliação não encontrada"))
            .andExpect(jsonPath("$.path").value("/estabelecimentos/1/agendamentos/10/avaliacoes/999"));
    }

    private Agendamento novoAgendamento(Long id) {
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
                LocalDateTime.parse("2026-04-20T14:30:00"),
                AgendamentoStatus.AGENDADO
        );
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        FindAgendamentoByIdUseCase findAgendamentoByIdUseCase() {
            return Mockito.mock(FindAgendamentoByIdUseCase.class);
        }

        @Bean
        CreateAvaliacaoUseCase createAvaliacaoUseCase() {
            return Mockito.mock(CreateAvaliacaoUseCase.class);
        }

        @Bean
        FindAllAvaliacoesUseCase findAllAvaliacoesUseCase() {
            return Mockito.mock(FindAllAvaliacoesUseCase.class);
        }

        @Bean
        FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase() {
            return Mockito.mock(FindAvaliacaoByIdUseCase.class);
        }

        @Bean
        UpdateAvaliacaoUseCase updateAvaliacaoUseCase() {
            return Mockito.mock(UpdateAvaliacaoUseCase.class);
        }

        @Bean
        DeleteAvaliacaoUseCase deleteAvaliacaoUseCase() {
            return Mockito.mock(DeleteAvaliacaoUseCase.class);
        }
    }
}