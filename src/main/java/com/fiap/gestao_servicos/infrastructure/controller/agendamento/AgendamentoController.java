package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.agendamento.CreateAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.DeleteAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAllAgendamentosUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.UpdateAgendamentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.agendamento.AgendamentoMapper;
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
@RequestMapping("/estabelecimentos/{estabelecimentoId}/agendamentos")
@Tag(name = "Agendamentos", description = "Operacoes de agendamento por estabelecimento")
public class AgendamentoController {

    private final CreateAgendamentoUseCase createAgendamentoUseCase;
    private final FindAllAgendamentosUseCase findAllAgendamentosUseCase;
    private final FindAgendamentoByIdUseCase findAgendamentoByIdUseCase;
    private final UpdateAgendamentoUseCase updateAgendamentoUseCase;
    private final DeleteAgendamentoUseCase deleteAgendamentoUseCase;

    public AgendamentoController(CreateAgendamentoUseCase createAgendamentoUseCase,
                                 FindAllAgendamentosUseCase findAllAgendamentosUseCase,
                                 FindAgendamentoByIdUseCase findAgendamentoByIdUseCase,
                                 UpdateAgendamentoUseCase updateAgendamentoUseCase,
                                 DeleteAgendamentoUseCase deleteAgendamentoUseCase) {
        this.createAgendamentoUseCase = createAgendamentoUseCase;
        this.findAllAgendamentosUseCase = findAllAgendamentosUseCase;
        this.findAgendamentoByIdUseCase = findAgendamentoByIdUseCase;
        this.updateAgendamentoUseCase = updateAgendamentoUseCase;
        this.deleteAgendamentoUseCase = deleteAgendamentoUseCase;
    }

    @PostMapping
        @Operation(summary = "Criar agendamento")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de criacao de agendamento",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AgendamentoDto.class),
                examples = @ExampleObject(
                    name = "agendamento",
                    value = """
                            {
                              "profissionalId": 5,
                              "servicoId": 20,
                              "clienteId": 12,
                              "dataHoraInicio": "2026-04-20T14:30:00",
                              "status": "AGENDADO"
                            }
                            """
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201",
                description = "Agendamento criado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AgendamentoResponseDto.class),
                    examples = @ExampleObject(
                        name = "agendamentoCriado",
                        value = """
                                {
                                  "id": 10,
                                  "profissionalId": 5,
                                  "profissionalNome": "Ana Souza",
                                  "servicoId": 20,
                                  "servicoNome": "Corte feminino",
                                  "estabelecimentoId": 1,
                                  "estabelecimentoNome": "Studio Beleza Centro",
                                  "clienteId": 12,
                                  "clienteNome": "Maria da Silva",
                                  "dataHoraInicio": "2026-04-20T14:30:00",
                                  "dataHoraFim": "2026-04-20T15:30:00",
                                  "status": "AGENDADO"
                                }
                                """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError")
        })
        public ResponseEntity<AgendamentoResponseDto> criar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                                        @Valid @RequestBody AgendamentoDto agendamentoDto) {
        agendamentoDto.setEstabelecimentoId(estabelecimentoId);
        var input = AgendamentoMapper.toDomain(agendamentoDto);
        Agendamento agendamentoCriado = createAgendamentoUseCase.create(input);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(AgendamentoMapper.toResponse(agendamentoCriado));
    }

    @GetMapping
        @Operation(summary = "Listar agendamentos por estabelecimento")
            @PageableAsQueryParam
                @ApiResponse(
                        responseCode = "200",
                        description = "Lista de agendamentos retornada com sucesso",
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        ref = "#/components/examples/PageResultExample"
                                )
                        )
                )
        public ResponseEntity<Page<AgendamentoResponseDto>> listar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                       @ParameterObject Pageable pageable) {
        Page<AgendamentoResponseDto> agendamentos = PageUtils.toSpringPage(
                findAllAgendamentosUseCase.findByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                        .map(AgendamentoMapper::toResponse));
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Buscar agendamento por ID")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Agendamento encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AgendamentoResponseDto.class),
                    examples = @ExampleObject(
                        name = "agendamento",
                        value = """
                                {
                                  "id": 10,
                                  "profissionalId": 5,
                                  "profissionalNome": "Ana Souza",
                                  "servicoId": 20,
                                  "servicoNome": "Corte feminino",
                                  "estabelecimentoId": 1,
                                  "estabelecimentoNome": "Studio Beleza Centro",
                                  "clienteId": 12,
                                  "clienteNome": "Maria da Silva",
                                  "dataHoraInicio": "2026-04-20T14:30:00",
                                  "dataHoraFim": "2026-04-20T15:30:00",
                                  "status": "AGENDADO"
                                }
                                """
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/NotFoundError")
        })
        public ResponseEntity<AgendamentoResponseDto> buscarPorId(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                      @Parameter(description = "ID do agendamento", example = "10") @PathVariable Long id) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(id);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, agendamento);
        return ResponseEntity.ok(AgendamentoMapper.toResponse(agendamento));
    }

    @PutMapping("/{id}")
        @Operation(summary = "Atualizar agendamento")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de atualizacao de agendamento",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AgendamentoDto.class),
                examples = @ExampleObject(
                    name = "agendamentoAtualizacao",
                    value = """
                            {
                              "profissionalId": 5,
                              "servicoId": 20,
                              "clienteId": 12,
                              "dataHoraInicio": "2026-04-20T15:00:00",
                              "status": "CONFIRMADO"
                            }
                            """
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Agendamento atualizado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AgendamentoResponseDto.class),
                    examples = @ExampleObject(
                        name = "agendamentoAtualizado",
                        value = """
                                {
                                  "id": 10,
                                  "profissionalId": 5,
                                  "profissionalNome": "Ana Souza",
                                  "servicoId": 20,
                                  "servicoNome": "Corte feminino",
                                  "estabelecimentoId": 1,
                                  "estabelecimentoNome": "Studio Beleza Centro",
                                  "clienteId": 12,
                                  "clienteNome": "Maria da Silva",
                                  "dataHoraInicio": "2026-04-20T15:00:00",
                                  "dataHoraFim": "2026-04-20T16:00:00",
                                  "status": "CONFIRMADO"
                                }
                                """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError")
        })
        public ResponseEntity<AgendamentoResponseDto> atualizar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                    @Parameter(description = "ID do agendamento", example = "10") @PathVariable Long id,
                                                            @Valid @RequestBody AgendamentoDto agendamentoDto) {
        Agendamento atual = findAgendamentoByIdUseCase.findById(id);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, atual);
        agendamentoDto.setEstabelecimentoId(estabelecimentoId);

        var input = AgendamentoMapper.toDomain(agendamentoDto);
        Agendamento agendamentoAtualizado = updateAgendamentoUseCase.update(id, input);
        return ResponseEntity.ok(AgendamentoMapper.toResponse(agendamentoAtualizado));
    }

    @DeleteMapping("/{id}")
        @Operation(summary = "Remover agendamento")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento removido com sucesso"),
            @ApiResponse(ref = "#/components/responses/NotFoundError")
        })
        public ResponseEntity<Void> deletar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                        @Parameter(description = "ID do agendamento", example = "10") @PathVariable Long id) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(id);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, agendamento);

        deleteAgendamentoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validateAgendamentoBelongsToEstabelecimento(Long estabelecimentoId, Agendamento agendamento) {
        Long agendamentoEstabelecimentoId = agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getId()
                : null;

        if (!estabelecimentoId.equals(agendamentoEstabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Agendamento não encontrado para o id: " + agendamento.getId()
                            + " no estabelecimento: " + estabelecimentoId
            );
        }
    }
}

