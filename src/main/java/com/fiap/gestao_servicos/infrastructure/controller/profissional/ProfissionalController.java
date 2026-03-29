package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.profissional.ProfissionalMapper;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProfissionalResponseDto> criar(@PathVariable Long estabelecimentoId,
                                                         @Valid @RequestBody ProfissionalDto profissionalDto) {
        Profissional novoProfissional = ProfissionalMapper.toDomain(profissionalDto);
        Profissional criado = createProfissionalNoEstabelecimentoUseCase.create(estabelecimentoId, novoProfissional);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProfissionalMapper.toResponse(criado));
    }

    @GetMapping
    public ResponseEntity<Page<ProfissionalResponseDto>> listar(@PathVariable Long estabelecimentoId,
                                                                Pageable pageable) {
        Page<ProfissionalResponseDto> profissionais = PageUtils.toSpringPage(
            findAllProfissionaisUseCase
                .findPageByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                .map(ProfissionalMapper::toResponse));
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> buscarPorId(@PathVariable Long estabelecimentoId,
                                                               @PathVariable Long id) {
        Profissional profissional = findProfissionalByIdUseCase.findByIdAndEstabelecimentoId(id, estabelecimentoId);
        return ResponseEntity.ok(ProfissionalMapper.toResponse(profissional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> atualizar(@PathVariable Long estabelecimentoId,
                                                             @PathVariable Long id,
                                                             @Valid @RequestBody ProfissionalDto profissionalDto) {
        Profissional atualizado = updateProfissionalNoEstabelecimentoUseCase.update(
                estabelecimentoId,
                id,
                ProfissionalMapper.toDomain(profissionalDto)
        );
        return ResponseEntity.ok(ProfissionalMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long estabelecimentoId,
                                        @PathVariable Long id) {
        deleteProfissionalNoEstabelecimentoUseCase.deleteById(estabelecimentoId, id);
        return ResponseEntity.noContent().build();
    }
}


