package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.CreateEstabelecimentoUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.DeleteEstabelecimentoUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindAllEstabelecimentosUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoDtoToDomainMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

    private final CreateEstabelecimentoUsecase createEstabelecimentoUsecase;
    private final FindAllEstabelecimentosUsecase findAllEstabelecimentosUsecase;
    private final FindEstabelecimentoByIdUsecase findEstabelecimentoByIdUsecase;
    private final UpdateEstabelecimentoUsecase updateEstabelecimentoUsecase;
    private final DeleteEstabelecimentoUsecase deleteEstabelecimentoUsecase;

    public EstabelecimentoController(CreateEstabelecimentoUsecase createEstabelecimentoUsecase,
                                     FindAllEstabelecimentosUsecase findAllEstabelecimentosUsecase,
                                     FindEstabelecimentoByIdUsecase findEstabelecimentoByIdUsecase,
                                     UpdateEstabelecimentoUsecase updateEstabelecimentoUsecase,
                                     DeleteEstabelecimentoUsecase deleteEstabelecimentoUsecase){
        this.createEstabelecimentoUsecase = createEstabelecimentoUsecase;
        this.findAllEstabelecimentosUsecase = findAllEstabelecimentosUsecase;
        this.findEstabelecimentoByIdUsecase = findEstabelecimentoByIdUsecase;
        this.updateEstabelecimentoUsecase = updateEstabelecimentoUsecase;
        this.deleteEstabelecimentoUsecase = deleteEstabelecimentoUsecase;
    }

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDto> criar(@RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoDtoToDomainMapper.toDomain(estabelecimentoDto);
        Estabelecimento estabelecimentoCriado = createEstabelecimentoUsecase.create(estabelecimento);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EstabelecimentoDomainToResponseMapper.toResponse(estabelecimentoCriado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteEstabelecimentoUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDto>> listar() {
        List<EstabelecimentoResponseDto> estabelecimentos = findAllEstabelecimentosUsecase.findAll().stream()
                .map(EstabelecimentoDomainToResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(estabelecimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDto> buscar(@PathVariable Long id) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUsecase.findById(id);
        return ResponseEntity.ok(EstabelecimentoDomainToResponseMapper.toResponse(estabelecimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDto> atualizar(@PathVariable Long id,
                                                                @RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoDtoToDomainMapper.toDomain(estabelecimentoDto);
        Estabelecimento atualizado = updateEstabelecimentoUsecase.update(id, estabelecimento);
        return ResponseEntity.ok(EstabelecimentoDomainToResponseMapper.toResponse(atualizado));
    }
}
