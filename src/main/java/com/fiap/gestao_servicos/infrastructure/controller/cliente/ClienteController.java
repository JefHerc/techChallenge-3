package com.fiap.gestao_servicos.infrastructure.controller.cliente;

import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.usecase.cliente.CreateClienteUsecase;
import com.fiap.gestao_servicos.core.usecase.cliente.DeleteClienteUsecase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindAllClientesUsecase;
import com.fiap.gestao_servicos.core.usecase.cliente.FindClienteByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.cliente.UpdateClienteUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.cliente.ClienteDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.cliente.ClienteDtoToDomainMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CreateClienteUsecase createClienteUsecase;
    private final FindAllClientesUsecase findAllClientesUsecase;
    private final FindClienteByIdUsecase findClienteByIdUsecase;
    private final UpdateClienteUsecase updateClienteUsecase;
    private final DeleteClienteUsecase deleteClienteUsecase;

    public ClienteController(CreateClienteUsecase createClienteUsecase,
                             FindAllClientesUsecase findAllClientesUsecase,
                             FindClienteByIdUsecase findClienteByIdUsecase,
                             UpdateClienteUsecase updateClienteUsecase,
                             DeleteClienteUsecase deleteClienteUsecase) {
        this.createClienteUsecase = createClienteUsecase;
        this.findAllClientesUsecase = findAllClientesUsecase;
        this.findClienteByIdUsecase = findClienteByIdUsecase;
        this.updateClienteUsecase = updateClienteUsecase;
        this.deleteClienteUsecase = deleteClienteUsecase;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> criar(@RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteDtoToDomainMapper.toDomain(clienteDto);
        Cliente clienteCriado = createClienteUsecase.create(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteDomainToResponseMapper.toResponse(clienteCriado));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listar() {
        List<ClienteResponseDto> clientes = findAllClientesUsecase.findAll().stream()
                .map(ClienteDomainToResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Long id) {
        Cliente cliente = findClienteByIdUsecase.findById(id);
        return ResponseEntity.ok(ClienteDomainToResponseMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteDtoToDomainMapper.toDomain(clienteDto);
        Cliente clienteAtualizado = updateClienteUsecase.update(id, cliente);
        return ResponseEntity.ok(ClienteDomainToResponseMapper.toResponse(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteClienteUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}