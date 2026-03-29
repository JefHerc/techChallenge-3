package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindEstabelecimentosByCriteriaUseCaseTest {

    @Test
    void deveDelegarBuscaParaRepositorioComFiltro() {
        EstabelecimentoRepository repository = mock(EstabelecimentoRepository.class);
        FindEstabelecimentosByCriteriaUseCase useCase = new FindEstabelecimentosByCriteriaUseCase(repository);

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        filter.setNome("Barbearia");
        filter.setCidade("São Paulo");

        Pageable pageable = PageRequest.of(0, 10);
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        Page<Estabelecimento> paginaEsperada = new PageImpl<>(List.of(estabelecimento));

        when(repository.findByCriteria(filter, pageable)).thenReturn(paginaEsperada);

        Page<Estabelecimento> resultado = useCase.findByCriteria(filter, pageable);

        assertEquals(paginaEsperada, resultado);
        verify(repository).findByCriteria(filter, pageable);
    }

    @Test
    void deveRetornarPaginaVaziaQuandoSemResultados() {
        EstabelecimentoRepository repository = mock(EstabelecimentoRepository.class);
        FindEstabelecimentosByCriteriaUseCase useCase = new FindEstabelecimentosByCriteriaUseCase(repository);

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByCriteria(filter, pageable)).thenReturn(Page.empty(pageable));

        Page<Estabelecimento> resultado = useCase.findByCriteria(filter, pageable);

        assertEquals(0, resultado.getTotalElements());
    }
}
