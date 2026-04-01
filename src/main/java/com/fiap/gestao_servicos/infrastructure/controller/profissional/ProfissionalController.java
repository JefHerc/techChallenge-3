package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.profissional.ProfissionalMapper;
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
@RequestMapping("/estabelecimentos/{estabelecimentoId}/profissionais")
@Tag(name = "Profissionais", description = "Operacoes de profissionais por estabelecimento")
public class ProfissionalController {

    private final CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase;
    private final FindAllProfissionaisUseCase findAllProfissionaisUseCase;
    private final FindProfissionalByIdUseCase findProfissionalByIdUseCase;
    private final UpdateProfissionalNoEstabelecimentoUseCase updateProfissionalNoEstabelecimentoUseCase;
    private final DeleteProfissionalNoEstabelecimentoUseCase deleteProfissionalNoEstabelecimentoUseCase;

    public ProfissionalController(CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase,
                                  FindAllProfissionaisUseCase findAllProfissionaisUseCase,
                                  FindProfissionalByIdUseCase findProfissionalByIdUseCase,
                                  UpdateProfissionalNoEstabelecimentoUseCase updateProfissionalNoEstabelecimentoUseCase,
                                  DeleteProfissionalNoEstabelecimentoUseCase deleteProfissionalNoEstabelecimentoUseCase) {
        this.createProfissionalNoEstabelecimentoUseCase = createProfissionalNoEstabelecimentoUseCase;
        this.findAllProfissionaisUseCase = findAllProfissionaisUseCase;
        this.findProfissionalByIdUseCase = findProfissionalByIdUseCase;
        this.updateProfissionalNoEstabelecimentoUseCase = updateProfissionalNoEstabelecimentoUseCase;
        this.deleteProfissionalNoEstabelecimentoUseCase = deleteProfissionalNoEstabelecimentoUseCase;
    }

    @PostMapping
        @Operation(summary = "Criar profissional")
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Payload de criacao de profissional",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ProfissionalDto.class),
                                examples = @ExampleObject(
                                        name = "profissional",
                                        value = """
                                                        {
                                                            "nome": "Carla Souza",
                                                            "cpf": "06854809096",
                                                            "celular": "11988887777",
                                                            "email": "carla@e1.com",
                                                            "urlFoto": "https://cdn.exemplo.com/fotos/carla.jpg",
                                                            "descricao": "Especialista coloração",
                                                            "sexo": "FEMININO",
                                                            "expedientes": [
                                                                {
                                                                    "diaSemana": "segunda-feira",
                                                                    "inicioTurno": "08:00",
                                                                    "fimTurno": "18:00"
                                                                }
                                                            ],
                                                            "servicosProfissional": [
                                                                {
                                                                    "servicoId": 1,
                                                                    "valor": 80.00
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
                description = "Profissional criado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProfissionalResponseDto.class),
                    examples = @ExampleObject(
                        name = "profissionalCriado",
                        value = """
                                {
                                  "id": 1,
                                  "nome": "Carla Souza",
                                  "cpf": "06854809096",
                                  "celular": "11988887777",
                                  "email": "carla@e1.com",
                                  "urlFoto": "https://cdn.exemplo.com/fotos/carla.jpg",
                                  "descricao": "Especialista coloração",
                                  "sexo": "FEMININO",
                                  "expedientes": [],
                                  "servicosProfissional": []
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
        public ResponseEntity<ProfissionalResponseDto> criar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                                         @Valid @RequestBody ProfissionalDto profissionalDto) {
        Profissional novoProfissional = ProfissionalMapper.toDomain(profissionalDto);
        Profissional criado = createProfissionalNoEstabelecimentoUseCase.create(estabelecimentoId, novoProfissional);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProfissionalMapper.toResponse(criado));
    }

    @GetMapping
        @Operation(summary = "Listar profissionais por estabelecimento")
            @PageableAsQueryParam
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de profissionais retornada com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        ref = "#/components/examples/PageResultExample"
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<Page<ProfissionalResponseDto>> listar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                    @ParameterObject Pageable pageable) {
        Page<ProfissionalResponseDto> profissionais = PageUtils.toSpringPage(
            findAllProfissionaisUseCase
                .findPageByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                .map(ProfissionalMapper::toResponse));
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Buscar profissional por ID")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Profissional encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProfissionalResponseDto.class),
                    examples = @ExampleObject(
                        name = "profissional",
                        value = """
                                {
                                  "id": 1,
                                  "nome": "Carla Souza",
                                  "cpf": "06854809096",
                                  "celular": "11988887777",
                                  "email": "carla@e1.com",
                                  "urlFoto": "https://cdn.exemplo.com/fotos/carla.jpg",
                                  "descricao": "Especialista coloração",
                                  "sexo": "FEMININO",
                                  "expedientes": [],
                                  "servicosProfissional": []
                                }
                                """
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
        public ResponseEntity<ProfissionalResponseDto> buscarPorId(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                       @Parameter(description = "ID do profissional", example = "5") @PathVariable Long id) {
        Profissional profissional = findProfissionalByIdUseCase.findByIdAndEstabelecimentoId(id, estabelecimentoId);
        return ResponseEntity.ok(ProfissionalMapper.toResponse(profissional));
    }

    @PutMapping("/{id}")
        @Operation(summary = "Atualizar profissional")
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Payload de atualizacao de profissional",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ProfissionalDto.class),
                                examples = @ExampleObject(
                                        name = "profissionalAtualizacao",
                                        value = """
                                                        {
                                                            "nome": "Carla Souza Premium",
                                                            "cpf": "06854809096",
                                                            "celular": "11988887771",
                                                            "email": "carla.premium@e1.com",
                                                            "urlFoto": "https://cdn.exemplo.com/fotos/carla-premium.jpg",
                                                            "descricao": "Especialista coloração e hidratação",
                                                            "sexo": "FEMININO",
                                                            "expedientes": [
                                                                {
                                                                    "diaSemana": "terca-feira",
                                                                    "inicioTurno": "10:00",
                                                                    "fimTurno": "19:00"
                                                                }
                                                            ],
                                                            "servicosProfissional": [
                                                                {
                                                                    "servicoId": 1,
                                                                    "valor": 130.00
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
                description = "Profissional atualizado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProfissionalResponseDto.class),
                    examples = @ExampleObject(
                        name = "profissionalAtualizado",
                        value = """
                                {
                                  "id": 1,
                                  "nome": "Carla Souza Premium",
                                  "cpf": "06854809096",
                                  "celular": "11988887771",
                                  "email": "carla.premium@e1.com",
                                  "urlFoto": "https://cdn.exemplo.com/fotos/carla-premium.jpg",
                                  "descricao": "Especialista coloração e hidratação",
                                  "sexo": "FEMININO",
                                  "expedientes": [],
                                  "servicosProfissional": []
                                }
                                """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/NotFoundError"),
                        @ApiResponse(ref = "#/components/responses/DuplicateDataError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError"),
        })
        public ResponseEntity<ProfissionalResponseDto> atualizar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                     @Parameter(description = "ID do profissional", example = "5") @PathVariable Long id,
                                                             @Valid @RequestBody ProfissionalDto profissionalDto) {
        Profissional atualizado = updateProfissionalNoEstabelecimentoUseCase.update(
                estabelecimentoId,
                id,
                ProfissionalMapper.toDomain(profissionalDto)
        );
        return ResponseEntity.ok(ProfissionalMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover profissional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profissional removido com sucesso"),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
                @ApiResponse(ref = "#/components/responses/DataIntegrityViolationException"),
                @ApiResponse(ref = "#/components/responses/InternalServerError")

    })
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do estabelecimento", example = "1") @PathVariable Long estabelecimentoId,
                                        @Parameter(description = "ID do profissional", example = "5") @PathVariable Long id) {
        deleteProfissionalNoEstabelecimentoUseCase.deleteById(estabelecimentoId, id);
        return ResponseEntity.noContent().build();
    }
}


