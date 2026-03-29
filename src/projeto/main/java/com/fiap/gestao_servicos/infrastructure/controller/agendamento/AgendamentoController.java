package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.usecase.agendamento.CreateAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.DeleteAgendamentoUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAllAgendamentosUseCase;
import com.fiap.gestao_servicos.core.usecase.agendamento.UpdateAgendamentoUseCase;
import com.fiap.gestao_servicos.infrastructure.mapper.agendamento.AgendamentoMapper;
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
@RequestMapping("/estabelecimentos/{estabelecimentoId}/agendamentos")
public class AgendamentoController {

    private final CreateAgendamentoUseCase createAgendamentoUseCase;
    private final FindAllAgendamentosUseCase findAllAgendamentosUseCase;
    private final FindAgendamentoByIdUseCase findAgendamentoByIdUseCase;
    private final UpdateAgendamentoUseCase updateAgendamentoUseCase;
    private final DeleteAgendamentoUseCase deleteAgendamentoUseCase;

    public AgendamentoController(CreateAgendamentoUseCase createAgendamentoUseCase,
                                 FindAllAgendamentosUseCase findAllAgendamentosUseCase,
                                 FindAgendamentoByIdUseCase findAgendamentoByIdUseCase,
                                 UpdateAgendamentoUseCase updateAgendamentoUseCase,
                                 DeleteAgendamentoUseCase deleteAgendamentoUseCase) {
        this.createAgendamentoUseCase = createAgendamentoUseCase;
        this.findAllAgendamentosUseCase = findAllAgendamentosUseCase;
        this.findAgendamentoByIdUseCase = findAgendamentoByIdUseCase;
        this.updateAgendamentoUseCase = updateAgendamentoUseCase;
        this.deleteAgendamentoUseCase = deleteAgendamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@PathVariable Long estabelecimentoId,
                                                        @Valid @RequestBody AgendamentoDto agendamentoDto) {
        agendamentoDto.setEstabelecimentoId(estabelecimentoId);
        var input = AgendamentoMapper.toDomain(agendamentoDto);
        Agendamento agendamentoCriado = createAgendamentoUseCase.create(input);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(AgendamentoMapper.toResponse(agendamentoCriado));
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDto>> listar(@PathVariable Long estabelecimentoId,
                                                               Pageable pageable) {
        Page<AgendamentoResponseDto> agendamentos = findAllAgendamentosUseCase.findByEstabelecimentoId(estabelecimentoId, pageable)
            .map(AgendamentoMapper::toResponse);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Long estabelecimentoId,
                                                              @PathVariable Long id) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(id);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, agendamento);
        return ResponseEntity.ok(AgendamentoMapper.toResponse(agendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizar(@PathVariable Long estabelecimentoId,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody AgendamentoDto agendamentoDto) {
        Agendamento atual = findAgendamentoByIdUseCase.findById(id);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, atual);
        agendamentoDto.setEstabelecimentoId(estabelecimentoId);

        var input = AgendamentoMapper.toDomain(agendamentoDto);
        Agendamento agendamentoAtualizado = updateAgendamentoUseCase.update(id, input);
        return ResponseEntity.ok(AgendamentoMapper.toResponse(agendamentoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long estabelecimentoId,
                                        @PathVariable Long id) {
        Agendamento agendamento = findAgendamentoByIdUseCase.findById(id);
        validateAgendamentoBelongsToEstabelecimento(estabelecimentoId, agendamento);

        deleteAgendamentoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validateAgendamentoBelongsToEstabelecimento(Long estabelecimentoId, Agendamento agendamento) {
        Long agendamentoEstabelecimentoId = agendamento.getEstabelecimento() != null
                ? agendamento.getEstabelecimento().getId()
                : null;

        if (!estabelecimentoId.equals(agendamentoEstabelecimentoId)) {
            throw new ResourceNotFoundException(
                    "Agendamento não encontrado para o id: " + agendamento.getId()
                            + " no estabelecimento: " + estabelecimentoId
            );
        }
    }
}

