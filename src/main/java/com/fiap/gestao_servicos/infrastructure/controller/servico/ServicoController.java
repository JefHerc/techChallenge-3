package com.fiap.gestao_servicos.infrastructure.controller.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicoUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.servico.ServicoDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.servico.ServicoDtoToDomainMapper;
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
@RequestMapping("/servicos")
public class ServicoController {

    private final CreateServicoUsecase createServicoUsecase;
    private final FindAllServicosUsecase findAllServicosUsecase;
    private final FindServicoByIdUsecase findServicoByIdUsecase;
    private final UpdateServicoUsecase updateServicoUsecase;
    private final DeleteServicoUsecase deleteServicoUsecase;

    public ServicoController(CreateServicoUsecase createServicoUsecase,
                             FindAllServicosUsecase findAllServicosUsecase,
                             FindServicoByIdUsecase findServicoByIdUsecase,
                             UpdateServicoUsecase updateServicoUsecase,
                             DeleteServicoUsecase deleteServicoUsecase) {
        this.createServicoUsecase = createServicoUsecase;
        this.findAllServicosUsecase = findAllServicosUsecase;
        this.findServicoByIdUsecase = findServicoByIdUsecase;
        this.updateServicoUsecase = updateServicoUsecase;
        this.deleteServicoUsecase = deleteServicoUsecase;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDto> criar(@RequestBody ServicoDto servicoDto) {
        Servico servico = ServicoDtoToDomainMapper.toDomain(servicoDto);
        Servico criado = createServicoUsecase.create(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoDomainToResponseMapper.toResponse(criado));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDto>> listar() {
        List<ServicoResponseDto> servicos = findAllServicosUsecase.findAll().stream()
                .map(ServicoDomainToResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarPorId(@PathVariable Long id) {
        Servico servico = findServicoByIdUsecase.findById(id);
        return ResponseEntity.ok(ServicoDomainToResponseMapper.toResponse(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizar(@PathVariable Long id, @RequestBody ServicoDto servicoDto) {
        Servico servico = ServicoDtoToDomainMapper.toDomain(servicoDto);
        Servico atualizado = updateServicoUsecase.update(id, servico);
        return ResponseEntity.ok(ServicoDomainToResponseMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteServicoUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}