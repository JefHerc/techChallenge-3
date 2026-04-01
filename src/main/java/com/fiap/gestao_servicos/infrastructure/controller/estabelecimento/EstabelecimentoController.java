package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.CreateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.DeleteEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindAllEstabelecimentosUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentosByCriteriaUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoFilterMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoMapper;
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
@RequestMapping("/estabelecimentos")
@Tag(name = "Estabelecimentos", description = "Operacoes de gerenciamento de estabelecimentos")
public class EstabelecimentoController {

    private final CreateEstabelecimentoUseCase createEstabelecimentoUseCase;
    private final FindAllEstabelecimentosUseCase findAllEstabelecimentosUseCase;
    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase;
    private final DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase;
    private final FindEstabelecimentosByCriteriaUseCase findEstabelecimentosByCriteriaUseCase;

    public EstabelecimentoController(CreateEstabelecimentoUseCase createEstabelecimentoUseCase,
                                     FindAllEstabelecimentosUseCase findAllEstabelecimentosUseCase,
                                     FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
                                     UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase,
                                     DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase,
                                     FindEstabelecimentosByCriteriaUseCase findEstabelecimentosByCriteriaUseCase) {
        this.createEstabelecimentoUseCase = createEstabelecimentoUseCase;
        this.findAllEstabelecimentosUseCase = findAllEstabelecimentosUseCase;
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.updateEstabelecimentoUseCase = updateEstabelecimentoUseCase;
        this.deleteEstabelecimentoUseCase = deleteEstabelecimentoUseCase;
        this.findEstabelecimentosByCriteriaUseCase = findEstabelecimentosByCriteriaUseCase;
    }

    @PostMapping
        @Operation(summary = "Criar estabelecimento")
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Payload de criacao de estabelecimento",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = EstabelecimentoDto.class),
                                examples = @ExampleObject(
                                        name = "estabelecimento",
                                        value = """
                                                        {
                                                            "nome": "Studio Bela Vida",
                                                            "endereco": {
                                                                "logradouro": "Avenida Paulista",
                                                                "numero": "1000",
                                                                "complemento": "Sala 10",
                                                                "bairro": "Bela Vista",
                                                                "cidade": "São Paulo",
                                                                "estado": "SP",
                                                                "cep": "01310100"
                                                            },
                                                            "cnpj": "60.354.362/0001-02",
                                                            "urlFotos": [
                                                                "https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"
                                                            ],
                                                            "horarioFuncionamento": [
                                                                {
                                                                "diaSemana": "segunda-feira",
                                                                "abertura": "08:00",
                                                                "fechamento": "20:00",
                                                                "fechado": false
                                                                }
                                                            ]
                                                        }
                                                    """
                                )
                        )
                )
        @ApiResponses(value = {
                        @ApiResponse(
                                responseCode = "201",
                                description = "Estabelecimento criado com sucesso",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = EstabelecimentoResponseDto.class),
                                        examples = @ExampleObject(
                                                name = "estabelecimentoCriado",
                                                value = """
                                                                {
                                                                    "id": 1,
                                                                    "nome": "Studio Bela Vida",
                                                                    "endereco": {
                                                                        "logradouro": "Avenida Paulista",
                                                                        "numero": "1000",
                                                                        "bairro": "Bela Vista",
                                                                        "cidade": "São Paulo",
                                                                        "estado": "SP",
                                                                        "cep": "01310100"
                                                                    },
                                                                    "cnpj": "64245654000168",
                                                                    "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"],
                                                                    "horarioFuncionamento": []
                                                                }
                                                                """
                                        )
                                )
                        ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/DuplicateDataError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
    public ResponseEntity<EstabelecimentoResponseDto> criar(@Valid @RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoMapper.toDomain(estabelecimentoDto);
        Estabelecimento estabelecimentoCriado = createEstabelecimentoUseCase.create(estabelecimento);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(EstabelecimentoMapper.toResponse(estabelecimentoCriado));
    }

    @DeleteMapping("/{id}")
        @Operation(summary = "Remover estabelecimento")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estabelecimento removido com sucesso"),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/DataIntegrityViolationException"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<Void> deletar(@Parameter(description = "ID do estabelecimento", example = "4") @PathVariable Long id) {
        deleteEstabelecimentoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
        @Operation(summary = "Listar estabelecimentos", description = "Retorna estabelecimentos paginados.")
            @PageableAsQueryParam
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista retornada com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        ref = "#/components/examples/PageResultExample"
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<Page<EstabelecimentoResponseDto>> listar(@ParameterObject Pageable pageable) {
        Page<EstabelecimentoResponseDto> estabelecimentos = PageUtils.toSpringPage(
                findAllEstabelecimentosUseCase.findAll(PageUtils.toPageQuery(pageable))
                        .map(EstabelecimentoMapper::toResponse));
        return ResponseEntity.ok(estabelecimentos);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Buscar estabelecimento por ID")
        @ApiResponses(value = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Estabelecimento encontrado",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = EstabelecimentoResponseDto.class),
                                        examples = @ExampleObject(
                                                name = "estabelecimento",
                                                value = """
                                                                {
                                                                    "id": 1,
                                                                    "nome": "Studio Bela Vida",
                                                                    "endereco": {
                                                                        "logradouro": "Avenida Paulista",
                                                                        "numero": "1000",
                                                                        "bairro": "Bela Vista",
                                                                        "cidade": "São Paulo",
                                                                        "estado": "SP",
                                                                        "cep": "01310100"
                                                                    },
                                                                    "cnpj": "64245654000168",
                                                                    "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/1/foto1.jpg"],
                                                                    "horarioFuncionamento": []
                                                                }
                                                                """
                                        )
                                )
                        ),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<EstabelecimentoResponseDto> buscar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long id) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(id);
        return ResponseEntity.ok(EstabelecimentoMapper.toResponse(estabelecimento));
    }

    @PutMapping("/{id}")
        @Operation(summary = "Atualizar estabelecimento")
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Payload de atualizacao de estabelecimento",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = EstabelecimentoDto.class),
                                examples = @ExampleObject(
                                        name = "estabelecimentoAtualizacao",
                                        value = """
                                                        {
                                                            "nome": "Studio Bela Vida Premium",
                                                            "endereco": {
                                                                "logradouro": "Avenida Paulista",
                                                                "numero": "1002",
                                                                "complemento": "Sala 12",
                                                                "bairro": "Bela Vista",
                                                                "cidade": "São Paulo",
                                                                "estado": "SP",
                                                                "cep": "01310100"
                                                            },
                                                            "cnpj": "64245654000249",
                                                            "urlFotos": [
                                                                "https://cdn.exemplo.com/estabelecimentos/5/foto1.jpg",
                                                                "https://cdn.exemplo.com/estabelecimentos/5/foto2.jpg"
                                                            ],
                                                            "horarioFuncionamento": [
                                                                {
                                                                    "diaSemana": "segunda-feira",
                                                                    "abertura": "08:00",
                                                                    "fechamento": "19:00"
                                                                }
                                                            ]
                                                        }
                                                        """
                                )
                        )
                )
        @ApiResponses(value = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "Estabelecimento atualizado com sucesso",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = EstabelecimentoResponseDto.class),
                                        examples = @ExampleObject(
                                                name = "estabelecimentoAtualizado",
                                                value = """
                                                                {
                                                                    "id": 5,
                                                                    "nome": "Studio Bela Vida Premium",
                                                                    "endereco": {
                                                                        "logradouro": "Avenida Paulista",
                                                                        "numero": "1002",
                                                                        "bairro": "Bela Vista",
                                                                        "cidade": "São Paulo",
                                                                        "estado": "SP",
                                                                        "cep": "01310100"
                                                                    },
                                                                    "cnpj": "64245654000249",
                                                                    "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/5/foto1.jpg"],
                                                                    "horarioFuncionamento": []
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
        public ResponseEntity<EstabelecimentoResponseDto> atualizar(@Parameter(description = "ID do estabelecimento", example = "5") @PathVariable Long id,
                                                                @Valid @RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoMapper.toDomain(estabelecimentoDto);
        Estabelecimento atualizado = updateEstabelecimentoUseCase.updateDadosCadastrais(id, estabelecimento);
        return ResponseEntity.ok(EstabelecimentoMapper.toResponse(atualizado));
    }

    @GetMapping("/busca")
        @Operation(summary = "Buscar estabelecimentos por filtros", description = "Busca estabelecimentos por criterios como nome, cidade, especialidade e servico.")
            @PageableAsQueryParam
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Resultado da busca retornado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        ref = "#/components/examples/PageResultExample"
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
    public ResponseEntity<Page<EstabelecimentoSearchResponseDto>> buscarComFiltros(
            @ParameterObject EstabelecimentoFilterDto filtroDto,
            @ParameterObject Pageable pageable) {
        EstabelecimentoFilter filter = EstabelecimentoFilterMapper.toDomain(filtroDto);

        Page<EstabelecimentoSearchResponseDto> resultado;

        if (filter.getServicoNome() != null && !filter.getServicoNome().isBlank()) {
            resultado = PageUtils.toSpringPage(
                    findEstabelecimentosByCriteriaUseCase
                            .findByCriteriaWithServices(filter, PageUtils.toPageQuery(pageable))
                            .map(EstabelecimentoMapper::toResponseForSearch)
            );
        } else {
            resultado = PageUtils.toSpringPage(
                    findEstabelecimentosByCriteriaUseCase
                            .findByCriteria(filter, PageUtils.toPageQuery(pageable))
                            .map(EstabelecimentoMapper::toResponseForSearch));
        }

        return ResponseEntity.ok(resultado);
    }
}


