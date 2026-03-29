package com.fiap.gestao_servicos.infrastructure.controller.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.usecase.cliente.CreateClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.DeleteClienteUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindAllClientesUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindClienteByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.cliente.UpdateClienteUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.cliente.ClienteMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
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
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.toDomain(clienteDto);
        Cliente clienteCriado = createClienteUseCase.create(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(clienteCriado));
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDto>> listar(Pageable pageable) {
        Page<ClienteResponseDto> clientes = PageUtils.toSpringPage(
                findAllClientesUseCase.findAll(PageUtils.toPageQuery(pageable))
                        .map(ClienteMapper::toResponse));
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Long id) {
        Cliente cliente = findClienteByIdUseCase.findById(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.toDomain(clienteDto);
        Cliente clienteAtualizado = updateClienteUseCase.update(id, cliente);
        return ResponseEntity.ok(ClienteMapper.toResponse(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteClienteUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

