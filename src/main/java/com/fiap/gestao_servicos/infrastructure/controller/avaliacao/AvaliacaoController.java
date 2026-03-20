package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.usecase.avaliacao.CreateAvaliacaoUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.DeleteAvaliacaoUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAllAvaliacoesUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAvaliacaoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.UpdateAvaliacaoUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.avaliacao.AvaliacaoDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.avaliacao.AvaliacaoDtoToDomainMapper;
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
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final CreateAvaliacaoUsecase createAvaliacaoUsecase;
    private final FindAllAvaliacoesUsecase findAllAvaliacoesUsecase;
    private final FindAvaliacaoByIdUsecase findAvaliacaoByIdUsecase;
    private final UpdateAvaliacaoUsecase updateAvaliacaoUsecase;
    private final DeleteAvaliacaoUsecase deleteAvaliacaoUsecase;

    public AvaliacaoController(CreateAvaliacaoUsecase createAvaliacaoUsecase,
                               FindAllAvaliacoesUsecase findAllAvaliacoesUsecase,
                               FindAvaliacaoByIdUsecase findAvaliacaoByIdUsecase,
                               UpdateAvaliacaoUsecase updateAvaliacaoUsecase,
                               DeleteAvaliacaoUsecase deleteAvaliacaoUsecase) {
        this.createAvaliacaoUsecase = createAvaliacaoUsecase;
        this.findAllAvaliacoesUsecase = findAllAvaliacoesUsecase;
        this.findAvaliacaoByIdUsecase = findAvaliacaoByIdUsecase;
        this.updateAvaliacaoUsecase = updateAvaliacaoUsecase;
        this.deleteAvaliacaoUsecase = deleteAvaliacaoUsecase;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDto> criar(@RequestBody AvaliacaoDto avaliacaoDto) {
        Avaliacao avaliacao = AvaliacaoDtoToDomainMapper.toDomain(avaliacaoDto);
        Avaliacao avaliacaoCriada = createAvaliacaoUsecase.create(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AvaliacaoDomainToResponseMapper.toResponse(avaliacaoCriada));
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDto>> listar() {
        List<AvaliacaoResponseDto> avaliacoes = findAllAvaliacoesUsecase.findAll().stream()
                .map(AvaliacaoDomainToResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDto> buscarPorId(@PathVariable Long id) {
        Avaliacao avaliacao = findAvaliacaoByIdUsecase.findById(id);
        return ResponseEntity.ok(AvaliacaoDomainToResponseMapper.toResponse(avaliacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDto> atualizar(@PathVariable Long id,
                                                          @RequestBody AvaliacaoDto avaliacaoDto) {
        Avaliacao avaliacao = AvaliacaoDtoToDomainMapper.toDomain(avaliacaoDto);
        Avaliacao avaliacaoAtualizada = updateAvaliacaoUsecase.update(id, avaliacao);
        return ResponseEntity.ok(AvaliacaoDomainToResponseMapper.toResponse(avaliacaoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteAvaliacaoUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
