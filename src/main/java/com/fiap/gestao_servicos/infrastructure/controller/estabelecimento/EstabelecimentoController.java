package com.fiap.gestao_servicos.infrastructure.controller.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.CreateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.DeleteEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindAllEstabelecimentosUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentosByCriteriaUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoFilterMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento.EstabelecimentoMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

    private final CreateEstabelecimentoUseCase createEstabelecimentoUseCase;
    private final FindAllEstabelecimentosUseCase findAllEstabelecimentosUseCase;
    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase;
    private final DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase;
    private final FindEstabelecimentosByCriteriaUseCase findEstabelecimentosByCriteriaUseCase;

    public EstabelecimentoController(CreateEstabelecimentoUseCase createEstabelecimentoUseCase,
                                     FindAllEstabelecimentosUseCase findAllEstabelecimentosUseCase,
                                     FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
                                     UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase,
                                     DeleteEstabelecimentoUseCase deleteEstabelecimentoUseCase,
                                     FindEstabelecimentosByCriteriaUseCase findEstabelecimentosByCriteriaUseCase) {
        this.createEstabelecimentoUseCase = createEstabelecimentoUseCase;
        this.findAllEstabelecimentosUseCase = findAllEstabelecimentosUseCase;
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.updateEstabelecimentoUseCase = updateEstabelecimentoUseCase;
        this.deleteEstabelecimentoUseCase = deleteEstabelecimentoUseCase;
        this.findEstabelecimentosByCriteriaUseCase = findEstabelecimentosByCriteriaUseCase;
    }

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDto> criar(@Valid @RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoMapper.toDomain(estabelecimentoDto);
        Estabelecimento estabelecimentoCriado = createEstabelecimentoUseCase.create(estabelecimento);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(EstabelecimentoMapper.toResponse(estabelecimentoCriado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteEstabelecimentoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EstabelecimentoResponseDto>> listar(Pageable pageable) {
        Page<EstabelecimentoResponseDto> estabelecimentos = PageUtils.toSpringPage(
                findAllEstabelecimentosUseCase.findAll(PageUtils.toPageQuery(pageable))
                        .map(EstabelecimentoMapper::toResponse));
        return ResponseEntity.ok(estabelecimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDto> buscar(@PathVariable Long id) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(id);
        return ResponseEntity.ok(EstabelecimentoMapper.toResponse(estabelecimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDto> atualizar(@PathVariable Long id,
                                                                @Valid @RequestBody EstabelecimentoDto estabelecimentoDto) {
        Estabelecimento estabelecimento = EstabelecimentoMapper.toDomain(estabelecimentoDto);
        Estabelecimento atualizado = updateEstabelecimentoUseCase.updateDadosCadastrais(id, estabelecimento);
        return ResponseEntity.ok(EstabelecimentoMapper.toResponse(atualizado));
    }

    @GetMapping("/busca")
    public ResponseEntity<Page<com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoSearchResponseDto>> buscarComFiltros(
            EstabelecimentoFilterDto filtroDto,
            Pageable pageable) {
        EstabelecimentoFilter filter = EstabelecimentoFilterMapper.toDomain(filtroDto);

        Page<com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoSearchResponseDto> resultado;
        
        if (filter.getServicoNome() != null && !filter.getServicoNome().isBlank()) {
            resultado = PageUtils.toSpringPage(
                    findEstabelecimentosByCriteriaUseCase
                            .findByCriteriaWithServices(filter, PageUtils.toPageQuery(pageable))
                            .map(EstabelecimentoMapper::toResponseForSearch)
            );
        } else {
            resultado = PageUtils.toSpringPage(
                    findEstabelecimentosByCriteriaUseCase
                            .findByCriteria(filter, PageUtils.toPageQuery(pageable))
                            .map(EstabelecimentoMapper::toResponseForSearch));
        }
        
        return ResponseEntity.ok(resultado);
    }
}


