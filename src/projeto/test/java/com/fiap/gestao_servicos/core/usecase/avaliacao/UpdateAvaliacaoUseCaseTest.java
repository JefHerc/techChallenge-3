package com.fiap.gestao_servicos.core.usecase.avaliacao;

import com.fiap.gestao_servicos.core.domain.Agendamento;
import com.fiap.gestao_servicos.core.domain.Avaliacao;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.AgendamentoRepository;
import com.fiap.gestao_servicos.core.repository.AvaliacaoRepository;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateAvaliacaoUseCaseTest {

    @Test
    void deveLancarExcecaoQuandoAvaliacaoNaoExistir() {
        AvaliacaoRepository avaliacaoRepository = mock(AvaliacaoRepository.class);
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
        EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);

        UpdateAvaliacaoUseCase useCase = new UpdateAvaliacaoUseCase(
                avaliacaoRepository,
                agendamentoRepository,
                profissionalRepository,
                estabelecimentoRepository
        );

        Avaliacao avaliacao = mock(Avaliacao.class);
        when(avaliacaoRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> useCase.update(99L, avaliacao));

        assertEquals("Avaliação não encontrado para o id: 99", ex.getMessage());
        verify(avaliacaoRepository, never()).update(99L, avaliacao);
    }

    @Test
    void deveLancarExcecaoQuandoProfissionalNaoPertencerAoEstabelecimento() {
        AvaliacaoRepository avaliacaoRepository = mock(AvaliacaoRepository.class);
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
        EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);

        UpdateAvaliacaoUseCase useCase = new UpdateAvaliacaoUseCase(
                avaliacaoRepository,
                agendamentoRepository,
                profissionalRepository,
                estabelecimentoRepository
        );

        Avaliacao avaliacao = mock(Avaliacao.class);
        Agendamento agendamento = mock(Agendamento.class);
        Profissional profissional = mock(Profissional.class);
        Estabelecimento estabelecimento = mock(Estabelecimento.class);

        when(avaliacaoRepository.existsById(11L)).thenReturn(true);

        when(avaliacao.getAgendamento()).thenReturn(agendamento);
        when(agendamento.getId()).thenReturn(10L);
        when(agendamento.getProfissional()).thenReturn(profissional);
        when(profissional.getId()).thenReturn(20L);
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);
        when(estabelecimento.getId()).thenReturn(30L);

        when(agendamentoRepository.existsById(10L)).thenReturn(true);
        when(agendamentoRepository.isConcluido(10L)).thenReturn(true);
        when(profissionalRepository.existsById(20L)).thenReturn(true);
        when(estabelecimentoRepository.existsById(30L)).thenReturn(true);
        when(estabelecimentoRepository.temProfissional(30L, 20L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.update(11L, avaliacao));

        assertEquals("Profissional informado não pertence ao estabelecimento informado", ex.getMessage());
        verify(avaliacaoRepository, never()).update(11L, avaliacao);
    }

    @Test
    void deveAtualizarQuandoReferenciasForemValidas() {
        AvaliacaoRepository avaliacaoRepository = mock(AvaliacaoRepository.class);
        AgendamentoRepository agendamentoRepository = mock(AgendamentoRepository.class);
        ProfissionalRepository profissionalRepository = mock(ProfissionalRepository.class);
        EstabelecimentoRepository estabelecimentoRepository = mock(EstabelecimentoRepository.class);

        UpdateAvaliacaoUseCase useCase = new UpdateAvaliacaoUseCase(
                avaliacaoRepository,
                agendamentoRepository,
                profissionalRepository,
                estabelecimentoRepository
        );

        Avaliacao avaliacao = mock(Avaliacao.class);
        Agendamento agendamento = mock(Agendamento.class);
        Profissional profissional = mock(Profissional.class);
        Estabelecimento estabelecimento = mock(Estabelecimento.class);

        when(avaliacaoRepository.existsById(11L)).thenReturn(true);

        when(avaliacao.getAgendamento()).thenReturn(agendamento);
        when(agendamento.getId()).thenReturn(10L);
        when(agendamento.getProfissional()).thenReturn(profissional);
        when(profissional.getId()).thenReturn(20L);
        when(agendamento.getEstabelecimento()).thenReturn(estabelecimento);
        when(estabelecimento.getId()).thenReturn(30L);

        when(agendamentoRepository.existsById(10L)).thenReturn(true);
        when(agendamentoRepository.isConcluido(10L)).thenReturn(true);
        when(profissionalRepository.existsById(20L)).thenReturn(true);
        when(estabelecimentoRepository.existsById(30L)).thenReturn(true);
        when(estabelecimentoRepository.temProfissional(30L, 20L)).thenReturn(true);
        when(agendamentoRepository.pertenceAoProfissionalEEstabelecimento(10L, 20L, 30L)).thenReturn(true);
        when(avaliacaoRepository.update(11L, avaliacao)).thenReturn(avaliacao);

        Avaliacao atualizada = useCase.update(11L, avaliacao);

        assertEquals(avaliacao, atualizada);
        verify(avaliacaoRepository).update(11L, avaliacao);
    }
}
