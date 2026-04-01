package com.fiap.gestao_servicos.infrastructure.controller.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.usecase.cliente.CreateClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.DeleteClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindAllClientesUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindClienteByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.UpdateClienteUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.cliente.ClienteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operacoes de gerenciamento de clientes")
public class ClienteController {

    private final CreateClienteUseCase createClienteUseCase;
    private final FindAllClientesUseCase findAllClientesUseCase;
    private final FindClienteByIdUseCase findClienteByIdUseCase;
    private final UpdateClienteUseCase updateClienteUseCase;
    private final DeleteClienteUseCase deleteClienteUseCase;

    public ClienteController(CreateClienteUseCase createClienteUseCase,
                             FindAllClientesUseCase findAllClientesUseCase,
                             FindClienteByIdUseCase findClienteByIdUseCase,
                             UpdateClienteUseCase updateClienteUseCase,
                             DeleteClienteUseCase deleteClienteUseCase) {
        this.createClienteUseCase = createClienteUseCase;
        this.findAllClientesUseCase = findAllClientesUseCase;
        this.findClienteByIdUseCase = findClienteByIdUseCase;
        this.updateClienteUseCase = updateClienteUseCase;
        this.deleteClienteUseCase = deleteClienteUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cadastra um novo cliente.")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de criacao de cliente",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class),
                examples = @ExampleObject(
                    name = "cliente",
                    value = """
                        {
                          "nome": "João Silva",
                          "cpf": "34028317088",
                          "celular": "11999998888",
                          "email": "joao@cliente.com",
                          "sexo": "MASCULINO"
                        }
                        """
                )
            )
        )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201",
                description = "Cliente criado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDto.class),
                    examples = @ExampleObject(
                        name = "clienteCriado",
                        value = """
                            {
                              "id": 1,
                              "nome": "João Silva",
                              "cpf": "34028317088",
                              "celular": "11999998888",
                              "email": "joao@cliente.com",
                              "sexo": "MASCULINO"
                            }
                            """
                    )
                )
            ),
                        @ApiResponse(ref = "#/components/responses/BadRequestError"),
                        @ApiResponse(ref = "#/components/responses/DuplicateDataError"),
                        @ApiResponse(ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.toDomain(clienteDto);
        Cliente clienteCriado = createClienteUseCase.create(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(clienteCriado));
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna clientes paginados.")
        @PageableAsQueryParam
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de clientes retornada com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        ref = "#/components/examples/PageResultExample"
                    )
                )
            ),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
        })
    public ResponseEntity<Page<ClienteResponseDto>> listar(Pageable pageable) {
        Page<ClienteResponseDto> clientes = PageUtils.toSpringPage(
                findAllClientesUseCase.findAll(PageUtils.toPageQuery(pageable))
                        .map(ClienteMapper::toResponse));
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Cliente encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDto.class),
                    examples = @ExampleObject(
                        name = "cliente",
                        value = """
                            {
                              "id": 1,
                              "nome": "João Silva",
                              "cpf": "34028317088",
                              "celular": "11999998888",
                              "email": "joao@cliente.com",
                              "sexo": "MASCULINO"
                            }
                            """
                    )
                )
            ),
                @ApiResponse(ref = "#/components/responses/NotFoundError"),
                @ApiResponse(ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<ClienteResponseDto> buscarPorId(@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
        Cliente cliente = findClienteByIdUseCase.findById(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente.")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload de atualizacao de cliente",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class),
                examples = @ExampleObject(
                    name = "clienteAtualizacao",
                    value = """
                        {
                          "nome": "João Silva Atualizado",
                          "cpf": "34028317088",
                          "celular": "11999990000",
                          "email": "joao.atualizado@cliente.com",
                          "sexo": "MASCULINO"
                        }
                        """
                )
            )
        )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Cliente atualizado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDto.class),
                    examples = @ExampleObject(
                        name = "clienteAtualizado",
                        value = """
                            {
                              "id": 1,
                              "nome": "João Silva Atualizado",
                              "cpf": "34028317088",
                              "celular": "11999990000",
                              "email": "joao.atualizado@cliente.com",
                              "sexo": "MASCULINO"
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
    public ResponseEntity<ClienteResponseDto> atualizar(@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id,
                                                        @Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.toDomain(clienteDto);
        Cliente clienteAtualizado = updateClienteUseCase.update(id, cliente);
        return ResponseEntity.ok(ClienteMapper.toResponse(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @ApiResponse(ref = "#/components/responses/NotFoundError"),
            @ApiResponse(ref = "#/components/responses/DataIntegrityViolationException"),
            @ApiResponse(ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
        deleteClienteUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

