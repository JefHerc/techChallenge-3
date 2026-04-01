package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.CreateAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.DeleteAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAllAvaliacoesUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAvaliacaoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.UpdateAvaliacaoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.avaliacao.AvaliacaoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estabelecimentos/{estabelecimentoId}")
@Tag(name = "Avaliacoes", description = "Operacoes de avaliacao vinculadas a agendamentos")
public class AvaliacaoController {

    private final FindAgendamentoByIdUseCase findAgendamentoByIdUseCase;
    private final CreateAvaliacaoUseCase createAvaliacaoUseCase;
    private final FindAllAvaliacoesUseCase findAllAvaliacoesUseCase;
    private final FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase;
    private final UpdateAvaliacaoUseCase updateAvaliacaoUseCase;
    private final DeleteAvaliacaoUseCase deleteAvaliacaoUseCase;

    public AvaliacaoController(FindAgendamentoByIdUseCase findAgendamentoByIdUseCase,
                               CreateAvaliacaoUseCase createAvaliacaoUseCase,
                               FindAllAvaliacoesUseCase findAllAvaliacoesUseCase,
                               FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase,
                               UpdateAvaliacaoUseCase updateAvaliacaoUseCase,
                               DeleteAvaliacaoUseCase deleteAvaliacaoUseCase) {
        this.findAgendamentoByIdUseCase = findAgendamentoByIdUseCase;
        this.createAvaliacaoUseCase = createAvaliacaoUseCase;
        this.findAllAvaliacoesUseCase = findAllAvaliacoesUseCase;
        this.findAvaliacaoByIdUseCase = findAvaliacaoByIdUseCase;
        this.updateAvaliacaoUseCase = updateAvaliacaoUseCase;
        this.deleteAvaliacaoUseCase = deleteAvaliacaoUseCase;
    }

    @PostMapping("/agendamentos/{agendamentoId}/avaliacoes")
        @Operation(summary = "Criar avaliacao")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de criacao de avaliacao",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvaliacaoDto.class),
                examples = @ExampleObject(
                    name = "avaliacao",
                    value = """
                            {
                              "notaEstabelecimento": 4.5,
                              "notaProfissional": 5.0,
                              "comentarioEstabelecimento": "Ambiente limpo e atendimento excelente",
                              "comentarioProfissional": "Profissional muito atenciosa"
                            }
                            """
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201",
                description = "Avaliacao criada com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AvaliacaoResponseDto.class),
                    examples = @ExampleObject(
                        name = "avaliacaoCriada",
                        value = """
                                {
                                  "id": 1,
                                  "agendamentoId": 1,
                                  "profissionalId": 1,
                                  "profissionalNome": "Carla Souza",
                                  "estabelecimentoId": 1,
                                  "estabelecimentoNome": "Studio Bela Vida",
                                  "statusAgendamento": "CONCLUIDO",
                                  "notaEstabelecimento": 4.8,
                                  "notaProfissional": 5.0,
                                  "comentarioEstabelecimento": "Ambiente excelente.",
                                  "comentarioProfissional": "Profissional muito atenciosa."
                                }
                                """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<AvaliacaoResponseDto> criar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                                      @Parameter(description = "ID do agendamento", example = "1") @PathVariable Long agendamentoId,
                                                      @Valid @RequestBody AvaliacaoDto avaliacaoDto) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(agendamentoId);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, agendamento);

        Avaliacao avaliacao = AvaliacaoMapper.toDomain(avaliacaoDto, agendamento);
        Avaliacao avaliacaoCriada = createAvaliacaoUseCase.create(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(AvaliacaoMapper.toResponse(avaliacaoCriada));
    }

    @GetMapping("/avaliacoes")
        @Operation(summary = "Listar avaliacoes de um estabelecimento")
            @PageableAsQueryParam
                @ApiResponses(value = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Lista de avaliacoes retornada com sucesso",
                                content = @Content(
                                        mediaType = "application/json",
                                        examples = @ExampleObject(
                                                ref = "#/components/examples/PageResultExample"
                                        )
                                )
                        ),
            @ApiResponse(ref = "#/components/responses/NotFoundError")
        })
        public ResponseEntity<Page<AvaliacaoResponseDto>> listar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                     @ParameterObject Pageable pageable) {
        Page<AvaliacaoResponseDto> avaliacoes = PageUtils.toSpringPage(
                findAllAvaliacoesUseCase.findByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                        .map(AvaliacaoMapper::toResponse));
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/agendamentos/{agendamentoId}/avaliacoes/{id}")
        @Operation(summary = "Buscar avaliacao por ID")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Avaliacao encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AvaliacaoResponseDto.class),
                    examples = @ExampleObject(
                        name = "avaliacao",
                        value = """
                                {
                                  "id": 1,
                                  "agendamentoId": 1,
                                  "profissionalId": 1,
                                  "profissionalNome": "Carla Souza",
                                  "estabelecimentoId": 1,
                                  "estabelecimentoNome": "Studio Bela Vida",
                                  "statusAgendamento": "CONCLUIDO",
                                  "notaEstabelecimento": 4.8,
                                  "notaProfissional": 5.0,
                                  "comentarioEstabelecimento": "Ambiente excelente.",
                                  "comentarioProfissional": "Profissional muito atenciosa."
                                }
                                """
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<AvaliacaoResponseDto> buscarPorId(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                    @Parameter(description = "ID do agendamento", example = "1") @PathVariable Long agendamentoId,
                                    @Parameter(description = "ID da avaliacao", example = "1") @PathVariable Long id) {
        Avaliacao avaliacao = findAvaliacaoByIdUseCase.findById(id);
        validateAvaliacaoBelongsToEstabelecimento(estabelecimentoId, avaliacao);
        validateAvaliacaoBelongsToAgendamento(agendamentoId, avaliacao);
        return ResponseEntity.ok(AvaliacaoMapper.toResponse(avaliacao));
    }

    @PutMapping("/agendamentos/{agendamentoId}/avaliacoes/{id}")
        @Operation(summary = "Atualizar avaliacao")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de atualizacao de avaliacao",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvaliacaoDto.class),
                examples = @ExampleObject(
                    name = "avaliacaoAtualizacao",
                    value = """
                            {
                              "notaEstabelecimento": 4.0,
                              "notaProfissional": 4.8,
                              "comentarioEstabelecimento": "Servico bom e rapido",
                              "comentarioProfissional": "Excelente tecnica"
                            }
                            """
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Avaliacao atualizada com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AvaliacaoResponseDto.class),
                    examples = @ExampleObject(
                        name = "avaliacaoAtualizada",
                        value = """
                                {
                                  "id": 1,
                                  "agendamentoId": 1,
                                  "profissionalId": 1,
                                  "profissionalNome": "Carla Souza",
                                  "estabelecimentoId": 1,
                                  "estabelecimentoNome": "Studio Bela Vida",
                                  "statusAgendamento": "CONCLUIDO",
                                  "notaEstabelecimento": 4.0,
                                  "notaProfissional": 4.8,
                                  "comentarioEstabelecimento": "Serviço bom e rápido",
                                  "comentarioProfissional": "Excelente técnica"
                                }
                                """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<AvaliacaoResponseDto> atualizar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                      @Parameter(description = "ID do agendamento", example = "1") @PathVariable Long agendamentoId,
                                  @Parameter(description = "ID da avaliacao", example = "1") @PathVariable Long id,
                                                          @Valid @RequestBody AvaliacaoDto avaliacaoDto) {
        Avaliacao avaliacaoAtual = findAvaliacaoByIdUseCase.findById(id);
        validateAvaliacaoBelongsToEstabelecimento(estabelecimentoId, avaliacaoAtual);
        validateAvaliacaoBelongsToAgendamento(agendamentoId, avaliacaoAtual);

        Avaliacao avaliacao = AvaliacaoMapper.toDomain(avaliacaoDto, avaliacaoAtual.getAgendamento());
        Avaliacao avaliacaoAtualizada = updateAvaliacaoUseCase.update(id, avaliacao);
        return ResponseEntity.ok(AvaliacaoMapper.toResponse(avaliacaoAtualizada));
    }

    @DeleteMapping("/agendamentos/avaliacoes/{id}")
        @Operation(summary = "Remover avaliacao")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avaliacao removida com sucesso"),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/DataIntegrityViolationException"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<Void> deletar(@Parameter(description = "ID do estabelecimento", example = "10") @PathVariable Long estabelecimentoId,
                        @Parameter(description = "ID da avaliacao", example = "30") @PathVariable Long id) {
        Avaliacao avaliacao = findAvaliacaoByIdUseCase.findById(id);
        validateAvaliacaoBelongsToEstabelecimento(estabelecimentoId, avaliacao);

        deleteAvaliacaoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validateAvaliacaoBelongsToEstabelecimento(Long estabelecimentoId, Avaliacao avaliacao) {
        Long avaliacaoEstabelecimentoId = avaliacao.getAgendamento() != null
                && avaliacao.getAgendamento().getEstabelecimento() != null
                ? avaliacao.getAgendamento().getEstabelecimento().getId()
                : null;
        if (!estabelecimentoId.equals(avaliacaoEstabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Avaliação não encontrada para o id: " + avaliacao.getId()
                            + " no estabelecimento: " + estabelecimentoId
            );
        }
    }

    private void validateAvaliacaoBelongsToAgendamento(Long agendamentoId, Avaliacao avaliacao) {
        Long avaliacaoAgendamentoId = avaliacao.getAgendamento() != null
                ? avaliacao.getAgendamento().getId()
                : null;
        if (!agendamentoId.equals(avaliacaoAgendamentoId)) {
            throw new ResourceNotFoundException(
                    "Avaliação não encontrada para o id: " + avaliacao.getId()
                            + " no agendamento: " + agendamentoId
            );
        }
    }

    private void validateAgendamentoBelongsToEstabelecimento(Long estabelecimentoId, Agendamento agendamento) {
        Long agendamentoEstabelecimentoId = agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getId()
                : null;
        if (!estabelecimentoId.equals(agendamentoEstabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Agendamento não encontrado para o estabelecimento: " + estabelecimentoId
            );
        }
    }
}


