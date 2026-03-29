package com.fiap.gestao_servicos.core.usecase.estabelecimento;

import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.repository.EstabelecimentoRepository;
import org.junit.jupiter.api.Test;

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

        PageQuery pageQuery = new PageQuery(0, 10, List.of());
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        PageResult<Estabelecimento> paginaEsperada = new PageResult<>(List.of(estabelecimento), 0, 10, 1, List.of());

        when(repository.findByCriteria(filter, pageQuery)).thenReturn(paginaEsperada);

        PageResult<Estabelecimento> resultado = useCase.findByCriteria(filter, pageQuery);

        assertEquals(paginaEsperada, resultado);
        verify(repository).findByCriteria(filter, pageQuery);
    }

    @Test
    void deveRetornarPaginaVaziaQuandoSemResultados() {
        EstabelecimentoRepository repository = mock(EstabelecimentoRepository.class);
        FindEstabelecimentosByCriteriaUseCase useCase = new FindEstabelecimentosByCriteriaUseCase(repository);

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        PageQuery pageQuery = new PageQuery(0, 10, List.of());

        when(repository.findByCriteria(filter, pageQuery)).thenReturn(new PageResult<>(List.of(), 0, 10, 0, List.of()));

        PageResult<Estabelecimento> resultado = useCase.findByCriteria(filter, pageQuery);

        assertEquals(0, resultado.getTotalElements());
    }
}
