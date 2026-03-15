package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.usecase.CreateEstabelecimentoUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.EstabelecimentoDtoToDomainMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

    private final CreateEstabelecimentoUsecase createEstabelecimentoUsecase;

    public EstabelecimentoController(CreateEstabelecimentoUsecase createEstabelecimentoUsecase){
        this.createEstabelecimentoUsecase = createEstabelecimentoUsecase;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoDtoToDomainMapper.toDomain(estabelecimentoDto);
        Estabelecimento estabelecimentoCriado = createEstabelecimentoUsecase.create(estabelecimento);
        return ResponseEntity.ok(estabelecimentoCriado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Estabelecimento>> listar() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estabelecimento> buscar(@PathVariable Long id) {
        return null;
    }
}
