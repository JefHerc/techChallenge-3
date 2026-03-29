package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.VerifyProfissionalVinculoUseCase;
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

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/estabelecimentos/{estabelecimentoId}/profissionais")
public class ProfissionalController {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase;
    private final VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase;
    private final UpdateProfissionalUseCase updateProfissionalUseCase;
    private final DeleteProfissionalUseCase deleteProfissionalUseCase;

    public ProfissionalController(FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
                                  CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase,
                                  VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase,
                                  UpdateProfissionalUseCase updateProfissionalUseCase,
                                  DeleteProfissionalUseCase deleteProfissionalUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.createProfissionalNoEstabelecimentoUseCase = createProfissionalNoEstabelecimentoUseCase;
        this.verifyProfissionalVinculoUseCase = verifyProfissionalVinculoUseCase;
        this.updateProfissionalUseCase = updateProfissionalUseCase;
        this.deleteProfissionalUseCase = deleteProfissionalUseCase;
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
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        List<ProfissionalResponseDto> profissionais = estabelecimento.getProfissionais().stream()
            .map(ProfissionalMapper::toResponse)
                .toList();
        return ResponseEntity.ok(PageUtils.paginate(profissionais, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> buscarPorId(@PathVariable Long estabelecimentoId,
                                                               @PathVariable Long id) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        Profissional profissional = estabelecimento.getProfissionais().stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profissional nao encontrado para o id: " + id + " no estabelecimento: " + estabelecimentoId
                ));
        return ResponseEntity.ok(ProfissionalMapper.toResponse(profissional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> atualizar(@PathVariable Long estabelecimentoId,
                                                             @PathVariable Long id,
                                                             @Valid @RequestBody ProfissionalDto profissionalDto) {
        Estabelecimento estabelecimento = validateProfissionalBelongsToEstabelecimento(estabelecimentoId, id);
        Profissional base = ProfissionalMapper.toDomain(profissionalDto);
        Profissional profissional = new Profissional(
                id,
                base.getNome(),
                base.getCpf(),
                base.getCelular(),
                base.getEmail(),
                base.getUrlFoto(),
                base.getDescricao(),
                base.getExpedientes(),
                base.getSexo(),
                base.getServicosProfissional()
        );
        verifyProfissionalVinculoUseCase.verify(estabelecimento, profissional);
        Profissional atualizado = updateProfissionalUseCase.update(id, profissional);
        return ResponseEntity.ok(ProfissionalMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long estabelecimentoId,
                                        @PathVariable Long id) {
        validateProfissionalBelongsToEstabelecimento(estabelecimentoId, id);
        deleteProfissionalUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Estabelecimento validateProfissionalBelongsToEstabelecimento(Long estabelecimentoId, Long profissionalId) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        boolean existe = estabelecimento.getProfissionais().stream().anyMatch(p -> profissionalId.equals(p.getId()));
        if (!existe) {
            throw new ResourceNotFoundException(
                    "Profissional nao encontrado para o id: " + profissionalId + " no estabelecimento: " + estabelecimentoId
            );
        }
        return estabelecimento;
    }
}


