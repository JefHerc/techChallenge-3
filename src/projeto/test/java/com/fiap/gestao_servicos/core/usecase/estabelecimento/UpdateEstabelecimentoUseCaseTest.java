package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateEstabelecimentoUseCaseTest {

    @Test
    void deveLancarExcecaoQuandoCnpjPertencerAOutroEstabelecimento() {
        EstabelecimentoRepository repository = mock(EstabelecimentoRepository.class);
        UpdateEstabelecimentoUseCase UseCase = new UpdateEstabelecimentoUseCase(repository);

        Estabelecimento atual = mock(Estabelecimento.class);
        Estabelecimento novo = mock(Estabelecimento.class);
        Cnpj cnpjAtual = mock(Cnpj.class);
        Cnpj cnpjNovo = mock(Cnpj.class);

        when(repository.findById(1L)).thenReturn(Optional.of(atual));
        when(atual.getCnpj()).thenReturn(cnpjAtual);
        when(cnpjAtual.getValue()).thenReturn("11222333000181");
        when(atual.getNome()).thenReturn("Salao A");

        when(novo.getCnpj()).thenReturn(cnpjNovo);
        when(cnpjNovo.getValue()).thenReturn("99888777000166");
        when(novo.getNome()).thenReturn("Salao B");

        when(repository.existsByCnpjAndNome("99888777000166", "Salao B")).thenReturn(false);
        when(repository.existsByCnpjAndIdNot("99888777000166", 1L)).thenReturn(true);

        DuplicateDataException ex = assertThrows(DuplicateDataException.class, () -> UseCase.update(1L, novo));

        assertEquals("Ja existe um estabelecimento com o mesmo CNPJ.", ex.getMessage());
        verify(repository, never()).update(1L, novo);
    }

    @Test
    void deveAtualizarQuandoNaoHouverDuplicidade() {
        EstabelecimentoRepository repository = mock(EstabelecimentoRepository.class);
        UpdateEstabelecimentoUseCase UseCase = new UpdateEstabelecimentoUseCase(repository);

        Estabelecimento atual = mock(Estabelecimento.class);
        Estabelecimento novo = mock(Estabelecimento.class);
        Cnpj cnpj = mock(Cnpj.class);

        when(repository.findById(2L)).thenReturn(Optional.of(atual));
        when(atual.getCnpj()).thenReturn(cnpj);
        when(cnpj.getValue()).thenReturn("11222333000181");
        when(atual.getNome()).thenReturn("Salao A");

        when(novo.getCnpj()).thenReturn(cnpj);
        when(novo.getNome()).thenReturn("Salao A");
        when(repository.existsByCnpjAndIdNot("11222333000181", 2L)).thenReturn(false);
        when(repository.update(2L, novo)).thenReturn(novo);

        Estabelecimento atualizado = UseCase.update(2L, novo);

        assertEquals(novo, atualizado);
        verify(repository).update(2L, novo);
    }

    @Test
    void deveAtualizarDadosCadastraisQuandoNaoHouverDuplicidade() {
        EstabelecimentoRepository repository = mock(EstabelecimentoRepository.class);
        UpdateEstabelecimentoUseCase UseCase = new UpdateEstabelecimentoUseCase(repository);

        Estabelecimento atual = mock(Estabelecimento.class);
        Estabelecimento novo = mock(Estabelecimento.class);
        Cnpj cnpj = mock(Cnpj.class);

        when(repository.findById(3L)).thenReturn(Optional.of(atual));
        when(atual.getCnpj()).thenReturn(cnpj);
        when(cnpj.getValue()).thenReturn("11222333000181");
        when(atual.getNome()).thenReturn("Salao A");

        when(novo.getCnpj()).thenReturn(cnpj);
        when(novo.getNome()).thenReturn("Salao A");
        when(repository.existsByCnpjAndIdNot("11222333000181", 3L)).thenReturn(false);
        when(repository.updateDadosCadastrais(3L, novo)).thenReturn(novo);

        Estabelecimento atualizado = UseCase.updateDadosCadastrais(3L, novo);

        assertEquals(novo, atualizado);
        verify(repository).updateDadosCadastrais(3L, novo);
        verify(repository, never()).update(3L, novo);
    }
}


