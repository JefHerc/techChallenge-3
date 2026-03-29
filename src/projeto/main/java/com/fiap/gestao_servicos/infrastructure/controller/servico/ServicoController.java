package com.fiap.gestao_servicos.infrastructure.controller.servico;

import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.UpdateEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.DeleteServicoUseCase;
import com.fiap.gestao_servicos.core.usecase.servico.UpdateServicoUseCase;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estabelecimentos/{estabelecimentoId}/servicos")
public class ServicoController {

    private final FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase;
    private final UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase;
    private final UpdateServicoUseCase updateServicoUseCase;
    private final DeleteServicoUseCase deleteServicoUseCase;

    public ServicoController(FindEstabelecimentoByIdUseCase findEstabelecimentoByIdUseCase,
                             UpdateEstabelecimentoUseCase updateEstabelecimentoUseCase,
                             UpdateServicoUseCase updateServicoUseCase,
                             DeleteServicoUseCase deleteServicoUseCase) {
        this.findEstabelecimentoByIdUseCase = findEstabelecimentoByIdUseCase;
        this.updateEstabelecimentoUseCase = updateEstabelecimentoUseCase;
        this.updateServicoUseCase = updateServicoUseCase;
        this.deleteServicoUseCase = deleteServicoUseCase;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDto> criar(@PathVariable Long estabelecimentoId,
                                                    @Valid @RequestBody ServicoDto servicoDto) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        Servico novoServico = ServicoMapper.toDomain(servicoDto);

        List<Servico> servicosAtualizados = new ArrayList<>(estabelecimento.getServicos());
        Set<Long> idsAntes = servicosAtualizados.stream()
                .map(Servico::getId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());
        servicosAtualizados.add(novoServico);

        Estabelecimento atualizado = updateEstabelecimentoUseCase.update(
                estabelecimentoId,
                copyWithServicos(estabelecimento, servicosAtualizados)
        );

        Servico criado = atualizado.getServicos().stream()
                .filter(s -> s.getId() != null && !idsAntes.contains(s.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Serviço criado não encontrado no estabelecimento: " + estabelecimentoId));

        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoMapper.toResponse(criado));
    }

    @GetMapping
        public ResponseEntity<Page<ServicoResponseDto>> listar(@PathVariable Long estabelecimentoId,
                                                                                                                   Pageable pageable) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        List<ServicoResponseDto> servicos = estabelecimento.getServicos().stream()
                .map(ServicoMapper::toResponse)
                .toList();
                return ResponseEntity.ok(PageUtils.paginate(servicos, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarPorId(@PathVariable Long estabelecimentoId,
                                                          @PathVariable Long id) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        Servico servico = estabelecimento.getServicos().stream()
                .filter(s -> id.equals(s.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Serviço não encontrado para o id: " + id + " no estabelecimento: " + estabelecimentoId
                ));
        return ResponseEntity.ok(ServicoMapper.toResponse(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizar(@PathVariable Long estabelecimentoId,
                                                        @PathVariable Long id,
                                                                                                                @Valid @RequestBody ServicoDto servicoDto) {
        validateServicoBelongsToEstabelecimento(estabelecimentoId, id);
        Servico base = ServicoMapper.toDomain(servicoDto);
        Servico servico = new Servico(id, base.getNome(), base.getDuracaoMedia());
        Servico atualizado = updateServicoUseCase.update(id, servico);
        return ResponseEntity.ok(ServicoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long estabelecimentoId,
                                        @PathVariable Long id) {
        validateServicoBelongsToEstabelecimento(estabelecimentoId, id);
        deleteServicoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validateServicoBelongsToEstabelecimento(Long estabelecimentoId, Long servicoId) {
        Estabelecimento estabelecimento = findEstabelecimentoByIdUseCase.findById(estabelecimentoId);
        boolean existe = estabelecimento.getServicos().stream().anyMatch(s -> servicoId.equals(s.getId()));
        if (!existe) {
            throw new ResourceNotFoundException(
                    "Serviço não encontrado para o id: " + servicoId + " no estabelecimento: " + estabelecimentoId
            );
        }
    }

    private Estabelecimento copyWithServicos(Estabelecimento origem, List<Servico> servicos) {
        return new Estabelecimento(
                origem.getId(),
                origem.getNome(),
                origem.getEndereco(),
                origem.getProfissionais(),
                servicos,
                origem.getCnpj(),
                origem.getUrlFotos(),
                origem.getHorarioFuncionamento()
        );
    }
}

