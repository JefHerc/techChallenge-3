package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.BusinessRuleException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAvaliacaoUseCaseTest {

    @Mock private AvaliacaoRepository avaliacaoRepository;
    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private ProfissionalRepository profissionalRepository;
    @Mock private EstabelecimentoRepository estabelecimentoRepository;
    @Mock private Avaliacao avaliacao;
    @Mock private Agendamento agendamento;
    @Mock private Profissional profissional;
    @Mock private Estabelecimento estabelecimento;

    private CreateAvaliacaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateAvaliacaoUseCase(
                avaliacaoRepository,
                agendamentoRepository,
                profissionalRepository,
                estabelecimentoRepository);

        when(avaliacao.getAgendamento()).thenReturn(agendamento);
        when(agendamento.getId()).thenReturn(10L);
        when(agendamento.getProfissional()).thenReturn(profissional);
        when(profissional.getId()).thenReturn(20L);
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);
        when(estabelecimento.getId()).thenReturn(30L);
    }

    @Test
    void deveCriarQuandoReferenciasForemValidas() {
        when(agendamentoRepository.existsById(10L)).thenReturn(true);
        when(agendamentoRepository.isConcluido(10L)).thenReturn(true);
        when(profissionalRepository.existsById(20L)).thenReturn(true);
        when(estabelecimentoRepository.existsById(30L)).thenReturn(true);
        when(estabelecimentoRepository.temProfissional(30L, 20L)).thenReturn(true);
        when(agendamentoRepository.pertenceAoProfissionalEEstabelecimento(10L, 20L, 30L)).thenReturn(true);
        when(avaliacaoRepository.create(avaliacao)).thenReturn(avaliacao);

        Avaliacao criada = useCase.create(avaliacao);

        assertEquals(avaliacao, criada);
        verify(avaliacaoRepository).create(avaliacao);
    }

    @Test
    void deveLancarExcecaoQuandoAgendamentoNaoExistir() {
        when(agendamentoRepository.existsById(10L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> useCase.create(avaliacao));

        assertEquals("Agendamento não encontrado para o id: 10", ex.getMessage());
        verify(avaliacaoRepository, never()).create(avaliacao);
    }

    @Test
    void deveLancarExcecaoQuandoAgendamentoNaoEstiverConcluido() {
        when(agendamentoRepository.existsById(10L)).thenReturn(true);
        when(agendamentoRepository.isConcluido(10L)).thenReturn(false);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> useCase.create(avaliacao));

        assertEquals("Agendamento deve estar com status CONCLUIDO para receber avaliação", ex.getMessage());
        verify(avaliacaoRepository, never()).create(avaliacao);
    }
}

