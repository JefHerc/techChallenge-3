package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.profissional.ProfissionalDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.profissional.ProfissionalDtoToDomainMapper;
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
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final CreateProfissionalUsecase createProfissionalUsecase;
    private final FindAllProfissionaisUsecase findAllProfissionaisUsecase;
    private final FindProfissionalByIdUsecase findProfissionalByIdUsecase;
    private final UpdateProfissionalUsecase updateProfissionalUsecase;
    private final DeleteProfissionalUsecase deleteProfissionalUsecase;

    public ProfissionalController(CreateProfissionalUsecase createProfissionalUsecase,
                                  FindAllProfissionaisUsecase findAllProfissionaisUsecase,
                                  FindProfissionalByIdUsecase findProfissionalByIdUsecase,
                                  UpdateProfissionalUsecase updateProfissionalUsecase,
                                  DeleteProfissionalUsecase deleteProfissionalUsecase) {
        this.createProfissionalUsecase = createProfissionalUsecase;
        this.findAllProfissionaisUsecase = findAllProfissionaisUsecase;
        this.findProfissionalByIdUsecase = findProfissionalByIdUsecase;
        this.updateProfissionalUsecase = updateProfissionalUsecase;
        this.deleteProfissionalUsecase = deleteProfissionalUsecase;
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponseDto> criar(@RequestBody ProfissionalDto profissionalDto) {
        Profissional profissional = ProfissionalDtoToDomainMapper.toDomain(profissionalDto);
        Profissional criado = createProfissionalUsecase.create(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfissionalDomainToResponseMapper.toResponse(criado));
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDto>> listar() {
        List<ProfissionalResponseDto> profissionais = findAllProfissionaisUsecase.findAll().stream()
                .map(ProfissionalDomainToResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> buscarPorId(@PathVariable Long id) {
        Profissional profissional = findProfissionalByIdUsecase.findById(id);
        return ResponseEntity.ok(ProfissionalDomainToResponseMapper.toResponse(profissional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> atualizar(@PathVariable Long id,
                                                              @RequestBody ProfissionalDto profissionalDto) {
        Profissional profissional = ProfissionalDtoToDomainMapper.toDomain(profissionalDto);
        Profissional atualizado = updateProfissionalUsecase.update(id, profissional);
        return ResponseEntity.ok(ProfissionalDomainToResponseMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteProfissionalUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}