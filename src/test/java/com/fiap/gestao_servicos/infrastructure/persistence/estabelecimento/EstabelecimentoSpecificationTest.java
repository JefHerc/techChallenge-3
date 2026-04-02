package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "rawtypes"})
class EstabelecimentoSpecificationTest {

    @Test
    void deveGerarPredicadoComFiltroVazio() {
        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        Specification<EstabelecimentoEntity> spec = EstabelecimentoSpecification.comFiltros(filter);

        Root root = mock(Root.class);
        CriteriaQuery query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate andPredicate = mock(Predicate.class);

        when(cb.and(any(Predicate[].class))).thenReturn(andPredicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }

    @Test
    void deveGerarPredicadoComTodosFiltrosPreenchidos() {
        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        filter.setNome("Studio");
        filter.setCidade("Sao Paulo");
        filter.setEstado("SP");
        filter.setBairro("Centro");
        filter.setServicoNome("CORTE");
        filter.setPrecoMin(new BigDecimal("10.00"));
        filter.setPrecoMax(new BigDecimal("100.00"));
        filter.setDataDisponivel(LocalDate.of(2026, 4, 1));
        filter.setNotaMinima(4.0);
        filter.setProfissionalNotaMinima(4.0);

        Specification<EstabelecimentoEntity> spec = EstabelecimentoSpecification.comFiltros(filter);

        Root root = mock(Root.class);
        Path anyPath = mock(Path.class);
        when(root.get(anyString())).thenReturn(anyPath);
        when(anyPath.get(anyString())).thenReturn(anyPath);

        CriteriaQuery query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate predicate = mock(Predicate.class);
        Predicate andPredicate = mock(Predicate.class);

        Expression lowerExpr = mock(Expression.class);
        when(cb.lower(any(Expression.class))).thenReturn(lowerExpr);
        when(cb.like(any(Expression.class), anyString())).thenReturn(predicate);
        when(cb.equal(any(), any())).thenReturn(predicate);
        when(cb.greaterThanOrEqualTo(any(Expression.class), any(BigDecimal.class))).thenReturn(predicate);
        when(cb.lessThanOrEqualTo(any(Expression.class), any(BigDecimal.class))).thenReturn(predicate);
        when(cb.greaterThanOrEqualTo(any(Expression.class), any(Double.class))).thenReturn(predicate);
        when(cb.not(any(Expression.class))).thenReturn(predicate);
        when(cb.exists(any(Subquery.class))).thenReturn(predicate);
        when(cb.and(any(Predicate[].class))).thenReturn(andPredicate);

        Expression avgExpr = mock(Expression.class);
        when(cb.avg(any(Expression.class))).thenReturn(avgExpr);

        Expression literalLongExpr = mock(Expression.class);
        when(cb.literal(1L)).thenReturn(literalLongExpr);

        Subquery subLong1 = mock(Subquery.class);
        Subquery subLong2 = mock(Subquery.class);
        Subquery subLong3 = mock(Subquery.class);
        Subquery subLong4 = mock(Subquery.class);
        Subquery subDouble = mock(Subquery.class);
        when(query.subquery(Long.class)).thenReturn(subLong1, subLong2, subLong3, subLong4);
        when(query.subquery(Double.class)).thenReturn(subDouble);

        Root subRootA = mock(Root.class);
        Path subPathA = mock(Path.class);
        when(subRootA.get(anyString())).thenReturn(subPathA);
        when(subPathA.get(anyString())).thenReturn(subPathA);

        when(subLong1.from(any(Class.class))).thenReturn(subRootA);
        when(subLong2.from(any(Class.class))).thenReturn(subRootA);
        when(subLong3.from(any(Class.class))).thenReturn(subRootA);
        when(subLong4.from(any(Class.class))).thenReturn(subRootA);
        when(subDouble.from(any(Class.class))).thenReturn(subRootA);

        when(subLong1.select(any(Expression.class))).thenReturn(subLong1);
        when(subLong2.select(any(Expression.class))).thenReturn(subLong2);
        when(subLong3.select(any(Expression.class))).thenReturn(subLong3);
        when(subLong4.select(any(Expression.class))).thenReturn(subLong4);
        when(subDouble.select(any(Expression.class))).thenReturn(subDouble);

        when(subLong1.where(any(Predicate[].class))).thenReturn(subLong1);
        when(subLong2.where(any(Predicate[].class))).thenReturn(subLong2);
        when(subLong3.where(any(Predicate[].class))).thenReturn(subLong3);
        when(subLong4.where(any(Predicate[].class))).thenReturn(subLong4);
        when(subDouble.where(any(Predicate[].class))).thenReturn(subDouble);

        when(subLong4.groupBy(any(Expression.class))).thenReturn(subLong4);

        Predicate result = spec.toPredicate(root, query, cb);

        assertNotNull(result);
    }
}
