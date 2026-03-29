package com.fiap.gestao_servicos.infrastructure.controller.avaliacao;

import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.CreateAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.DeleteAvaliacaoUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAllAvaliacoesUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.FindAvaliacaoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.avaliacao.UpdateAvaliacaoUseCase;
import com.fiap.gestao_servicos.infrastructure.controller.PageUtils;
import com.fiap.gestao_servicos.infrastructure.mapper.avaliacao.AvaliacaoMapper;
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
@RequestMapping("/agendamentos/{agendamentoId}/avaliacoes")
public class AvaliacaoController {

    private final FindAgendamentoByIdUseCase findAgendamentoByIdUseCase;
    private final CreateAvaliacaoUseCase createAvaliacaoUseCase;
    private final FindAllAvaliacoesUseCase findAllAvaliacoesUseCase;
    private final FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase;
    private final UpdateAvaliacaoUseCase updateAvaliacaoUseCase;
    private final DeleteAvaliacaoUseCase deleteAvaliacaoUseCase;

    public AvaliacaoController(FindAgendamentoByIdUseCase findAgendamentoByIdUseCase,
                               CreateAvaliacaoUseCase createAvaliacaoUseCase,
                               FindAllAvaliacoesUseCase findAllAvaliacoesUseCase,
                               FindAvaliacaoByIdUseCase findAvaliacaoByIdUseCase,
                               UpdateAvaliacaoUseCase updateAvaliacaoUseCase,
                               DeleteAvaliacaoUseCase deleteAvaliacaoUseCase) {
        this.findAgendamentoByIdUseCase = findAgendamentoByIdUseCase;
        this.createAvaliacaoUseCase = createAvaliacaoUseCase;
        this.findAllAvaliacoesUseCase = findAllAvaliacoesUseCase;
        this.findAvaliacaoByIdUseCase = findAvaliacaoByIdUseCase;
        this.updateAvaliacaoUseCase = updateAvaliacaoUseCase;
        this.deleteAvaliacaoUseCase = deleteAvaliacaoUseCase;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDto> criar(@PathVariable Long agendamentoId,
                                                      @Valid @RequestBody AvaliacaoDto avaliacaoDto) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(agendamentoId);
        fillAvaliacaoDtoFromAgendamento(avaliacaoDto, agendamento);

        Avaliacao avaliacao = AvaliacaoMapper.toDomain(avaliacaoDto);
        Avaliacao avaliacaoCriada = createAvaliacaoUseCase.create(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(AvaliacaoMapper.toResponse(avaliacaoCriada));
    }

    @GetMapping
    public ResponseEntity<Page<AvaliacaoResponseDto>> listar(@PathVariable Long agendamentoId,
                                                             Pageable pageable) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(agendamentoId);
        Long estabelecimentoId = agendamento.getEstabelecimento().getId();

        Page<AvaliacaoResponseDto> avaliacoes = PageUtils.toSpringPage(
                findAllAvaliacoesUseCase.findByEstabelecimentoId(estabelecimentoId, PageUtils.toPageQuery(pageable))
                        .map(AvaliacaoMapper::toResponse));
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDto> buscarPorId(@PathVariable Long agendamentoId,
                                                            @PathVariable Long id) {
        Avaliacao avaliacao = findAvaliacaoByIdUseCase.findById(id);
        validateAvaliacaoBelongsToAgendamento(agendamentoId, avaliacao);
        return ResponseEntity.ok(AvaliacaoMapper.toResponse(avaliacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDto> atualizar(@PathVariable Long agendamentoId,
                                                          @PathVariable Long id,
                                                          @Valid @RequestBody AvaliacaoDto avaliacaoDto) {
        Avaliacao avaliacaoAtual = findAvaliacaoByIdUseCase.findById(id);
        validateAvaliacaoBelongsToAgendamento(agendamentoId, avaliacaoAtual);

        Agendamento agendamento = findAgendamentoByIdUseCase.findById(agendamentoId);
        fillAvaliacaoDtoFromAgendamento(avaliacaoDto, agendamento);

        Avaliacao avaliacao = AvaliacaoMapper.toDomain(avaliacaoDto);
        Avaliacao avaliacaoAtualizada = updateAvaliacaoUseCase.update(id, avaliacao);
        return ResponseEntity.ok(AvaliacaoMapper.toResponse(avaliacaoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long agendamentoId,
                                        @PathVariable Long id) {
        Avaliacao avaliacao = findAvaliacaoByIdUseCase.findById(id);
        validateAvaliacaoBelongsToAgendamento(agendamentoId, avaliacao);

        deleteAvaliacaoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void fillAvaliacaoDtoFromAgendamento(AvaliacaoDto dto, Agendamento agendamento) {
        dto.setAgendamentoId(agendamento.getId());
        dto.setProfissionalId(agendamento.getProfissional().getId());
        dto.setEstabelecimentoId(agendamento.getEstabelecimento().getId());
    }

    private void validateAvaliacaoBelongsToAgendamento(Long agendamentoId, Avaliacao avaliacao) {
        Long avaliacaoAgendamentoId = avaliacao.getAgendamento() != null ? avaliacao.getAgendamento().getId() : null;
        if (!agendamentoId.equals(avaliacaoAgendamentoId)) {
            throw new ResourceNotFoundException(
                    "Avaliação não encontrada para o id: " + avaliacao.getId() + " no agendamento: " + agendamentoId
            );
        }
    }
}


