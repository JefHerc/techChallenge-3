package com.fiap.gestao_servicos.infrastructure.controller.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicosNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.servico.ServicoMapper;
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

import java.util.List;

@RestController
@RequestMapping("/estabelecimentos/{estabelecimentoId}/servicos")
@Tag(name = "Servicos", description = "Operacoes de servicos ofertados por estabelecimento")
public class ServicoController {

    private final CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase;
    private final FindAllServicosUseCase findAllServicosUseCase;
    private final FindServicoByIdUseCase findServicoByIdUseCase;
    private final UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase;
    private final DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase;

    public ServicoController(CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase,
                             FindAllServicosUseCase findAllServicosUseCase,
                             FindServicoByIdUseCase findServicoByIdUseCase,
                             UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase,
                             DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase) {
        this.createServicosNoEstabelecimentoUseCase = createServicosNoEstabelecimentoUseCase;
        this.findAllServicosUseCase = findAllServicosUseCase;
        this.findServicoByIdUseCase = findServicoByIdUseCase;
        this.updateServicoNoEstabelecimentoUseCase = updateServicoNoEstabelecimentoUseCase;
        this.deleteServicoNoEstabelecimentoUseCase = deleteServicoNoEstabelecimentoUseCase;
    }

    @PostMapping
        @Operation(summary = "Criar servicos no estabelecimento")
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Lista de servicos para cadastro no estabelecimento",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(
                                        name = "servicos",
                                        value = """
                                                        [
                                                            {
                                                                "nome": "MASSAGEM",
                                                                "duracaoMedia": "PT1H"
                                                            },
                                                            {
                                                                "nome": "BARBA",
                                                                "duracaoMedia": "PT30M"
                                                            }
                                                        ]
                                                        """
                                )
                        )
                )
        @ApiResponses(value = {
                        @ApiResponse(
                                responseCode = "201",
                                description = "Servicos criados com sucesso",
                                content = @Content(
                                        mediaType = "application/json",
                                        examples = @ExampleObject(
                                                name = "servicosCriados",
                                                value = """
                                                                [
                                                                    {
                                                                        "id": 1,
                                                                        "nome": "CORTE",
                                                                        "duracaoMedia": "PT1H"
                                                                    },
                                                                    {
                                                                        "id": 4,
                                                                        "nome": "ESCOVA",
                                                                        "duracaoMedia": "PT30M"
                                                                    }
                                                                ]
                                                                """
                                        )
                                )
                        ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError"),
                        @ApiResponse(ref = "#/components/responses/DuplicateDataError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<List<ServicoResponseDto>> criar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                                          @Valid @RequestBody List<ServicoDto> servicoDtos) {
        List<ServicoResponseDto> criados = createServicosNoEstabelecimentoUseCase.create(
                        estabelecimentoId,
                        servicoDtos.stream().map(ServicoMapper::toDomain).toList())
                .stream()
                .map(ServicoMapper::toResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(criados);
    }

    @GetMapping
        @Operation(summary = "Listar servicos por estabelecimento")
            @PageableAsQueryParam
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de servicos retornada com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        ref = "#/components/examples/PageResultExample"
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<Page<ServicoResponseDto>> listar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                   @ParameterObject Pageable pageable) {
        Page<ServicoResponseDto> servicos = PageUtils.toSpringPage(
            findAllServicosUseCase
                .findPageByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                .map(ServicoMapper::toResponse));
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Buscar servico por ID")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Servico encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ServicoResponseDto.class),
                    examples = @ExampleObject(
                        name = "servico",
                        value = """
                                {
                                  "id": 1,
                                  "nome": "CORTE",
                                  "duracaoMedia": "PT1H"
                                }
                                """
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<ServicoResponseDto> buscarPorId(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                  @Parameter(description = "ID do servico", example = "1") @PathVariable Long id) {
        Servico servico = findServicoByIdUseCase.findByIdAndEstabelecimentoId(id, estabelecimentoId);
        return ResponseEntity.ok(ServicoMapper.toResponse(servico));
    }

    @PutMapping("/{id}")
        @Operation(summary = "Atualizar servico")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de atualizacao de servico",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ServicoDto.class),
                examples = @ExampleObject(
                    name = "servicoAtualizacao",
                    value = """
                            {
                              "nome": "LIMPEZA_DE_PELE",
                              "duracaoMedia": "PT1H15M"
                            }
                            """
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Servico atualizado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ServicoResponseDto.class),
                    examples = @ExampleObject(
                        name = "servicoAtualizado",
                        value = """
                                {
                                  "id": 2,
                                  "nome": "LIMPEZA_DE_PELE",
                                  "duracaoMedia": "PT1H15M"
                                }
                                """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError"),
                        @ApiResponse(ref = "#/components/responses/DuplicateDataError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<ServicoResponseDto> atualizar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                @Parameter(description = "ID do servico", example = "2") @PathVariable Long id,
                                                        @Valid @RequestBody ServicoDto servicoDto) {
        Servico atualizado = updateServicoNoEstabelecimentoUseCase.update(
                estabelecimentoId,
                id,
                ServicoMapper.toDomain(servicoDto)
        );
        return ResponseEntity.ok(ServicoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover servico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Servico removido com sucesso"),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/DataIntegrityViolationException"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do estabelecimento", example = "3") @PathVariable Long estabelecimentoId,
                                        @Parameter(description = "ID do servico", example = "20") @PathVariable Long id) {
        deleteServicoNoEstabelecimentoUseCase.deleteById(estabelecimentoId, id);
        return ResponseEntity.noContent().build();
    }
}

