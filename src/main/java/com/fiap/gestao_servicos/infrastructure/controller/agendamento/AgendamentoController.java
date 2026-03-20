package com.fiap.gestao_servicos.infrastructure.controller.agendamento;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.usecase.agendamento.CreateAgendamentoUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.DeleteAgendamentoUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAgendamentoByIdUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.FindAllAgendamentosUsecase;
import com.fiap.gestao_servicos.core.usecase.agendamento.UpdateAgendamentoUsecase;
import com.fiap.gestao_servicos.infrastructure.mapper.agendamento.AgendamentoDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.agendamento.AgendamentoDtoToDomainMapper;
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
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final CreateAgendamentoUsecase createAgendamentoUsecase;
    private final FindAllAgendamentosUsecase findAllAgendamentosUsecase;
    private final FindAgendamentoByIdUsecase findAgendamentoByIdUsecase;
    private final UpdateAgendamentoUsecase updateAgendamentoUsecase;
    private final DeleteAgendamentoUsecase deleteAgendamentoUsecase;

    public AgendamentoController(CreateAgendamentoUsecase createAgendamentoUsecase,
                                 FindAllAgendamentosUsecase findAllAgendamentosUsecase,
                                 FindAgendamentoByIdUsecase findAgendamentoByIdUsecase,
                                 UpdateAgendamentoUsecase updateAgendamentoUsecase,
                                 DeleteAgendamentoUsecase deleteAgendamentoUsecase) {
        this.createAgendamentoUsecase = createAgendamentoUsecase;
        this.findAllAgendamentosUsecase = findAllAgendamentosUsecase;
        this.findAgendamentoByIdUsecase = findAgendamentoByIdUsecase;
        this.updateAgendamentoUsecase = updateAgendamentoUsecase;
        this.deleteAgendamentoUsecase = deleteAgendamentoUsecase;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criar(@RequestBody AgendamentoDto agendamentoDto) {
        Agendamento agendamento = AgendamentoDtoToDomainMapper.toDomain(agendamentoDto);
        Agendamento agendamentoCriado = createAgendamentoUsecase.create(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AgendamentoDomainToResponseMapper.toResponse(agendamentoCriado));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listar() {
        List<AgendamentoResponseDto> agendamentos = findAllAgendamentosUsecase.findAll().stream()
                .map(AgendamentoDomainToResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Long id) {
        Agendamento agendamento = findAgendamentoByIdUsecase.findById(id);
        return ResponseEntity.ok(AgendamentoDomainToResponseMapper.toResponse(agendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizar(@PathVariable Long id,
                                                            @RequestBody AgendamentoDto agendamentoDto) {
        Agendamento agendamento = AgendamentoDtoToDomainMapper.toDomain(agendamentoDto);
        Agendamento agendamentoAtualizado = updateAgendamentoUsecase.update(id, agendamento);
        return ResponseEntity.ok(AgendamentoDomainToResponseMapper.toResponse(agendamentoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deleteAgendamentoUsecase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}