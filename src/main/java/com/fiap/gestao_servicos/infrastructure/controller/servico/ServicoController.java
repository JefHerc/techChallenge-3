package com.fiap.gestao_servicos.infrastructure.controller.servico;

import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.usecase.servico.CreateServicosNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindAllServicosUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.FindServicoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.servico.ServicoMapper;
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

@RestController
@RequestMapping("/estabelecimentos/{estabelecimentoId}/servicos")
public class ServicoController {

    private final CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase;
    private final FindAllServicosUseCase findAllServicosUseCase;
    private final FindServicoByIdUseCase findServicoByIdUseCase;
    private final UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase;
    private final DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase;

    public ServicoController(CreateServicosNoEstabelecimentoUseCase createServicosNoEstabelecimentoUseCase,
                             FindAllServicosUseCase findAllServicosUseCase,
                             FindServicoByIdUseCase findServicoByIdUseCase,
                             UpdateServicoNoEstabelecimentoUseCase updateServicoNoEstabelecimentoUseCase,
                             DeleteServicoNoEstabelecimentoUseCase deleteServicoNoEstabelecimentoUseCase) {
        this.createServicosNoEstabelecimentoUseCase = createServicosNoEstabelecimentoUseCase;
        this.findAllServicosUseCase = findAllServicosUseCase;
        this.findServicoByIdUseCase = findServicoByIdUseCase;
        this.updateServicoNoEstabelecimentoUseCase = updateServicoNoEstabelecimentoUseCase;
        this.deleteServicoNoEstabelecimentoUseCase = deleteServicoNoEstabelecimentoUseCase;
    }

    @PostMapping
    public ResponseEntity<List<ServicoResponseDto>> criar(@PathVariable Long estabelecimentoId,
                                                          @Valid @RequestBody List<ServicoDto> servicoDtos) {
        List<ServicoResponseDto> criados = createServicosNoEstabelecimentoUseCase.create(
                        estabelecimentoId,
                        servicoDtos.stream().map(ServicoMapper::toDomain).toList())
                .stream()
                .map(ServicoMapper::toResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(criados);
    }

    @GetMapping
    public ResponseEntity<Page<ServicoResponseDto>> listar(@PathVariable Long estabelecimentoId,
                                                           Pageable pageable) {
        Page<ServicoResponseDto> servicos = PageUtils.toSpringPage(
            findAllServicosUseCase
                .findPageByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                .map(ServicoMapper::toResponse));
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarPorId(@PathVariable Long estabelecimentoId,
                                                          @PathVariable Long id) {
        Servico servico = findServicoByIdUseCase.findByIdAndEstabelecimentoId(id, estabelecimentoId);
        return ResponseEntity.ok(ServicoMapper.toResponse(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizar(@PathVariable Long estabelecimentoId,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody ServicoDto servicoDto) {
        Servico atualizado = updateServicoNoEstabelecimentoUseCase.update(
                estabelecimentoId,
                id,
                ServicoMapper.toDomain(servicoDto)
        );
        return ResponseEntity.ok(ServicoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long estabelecimentoId,
                                        @PathVariable Long id) {
        deleteServicoNoEstabelecimentoUseCase.deleteById(estabelecimentoId, id);
        return ResponseEntity.noContent().build();
    }
}

