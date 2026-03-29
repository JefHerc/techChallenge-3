package com.fiap.gestao_servicos.infrastructure.pagination;

import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.pagination.SortDirection;
import com.fiap.gestao_servicos.core.pagination.SortField;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpringPaginationMapperTest {

    @Test
    void toPageQueryDeveMapearPageSizeNumeroESort() {
        Pageable pageable = PageRequest.of(2, 15, Sort.by(
                Sort.Order.asc("nome"),
                Sort.Order.desc("id")
        ));

        PageQuery pageQuery = SpringPaginationMapper.toPageQuery(pageable);

        assertEquals(2, pageQuery.getPageNumber());
        assertEquals(15, pageQuery.getPageSize());
        assertEquals(2, pageQuery.getSortFields().size());
        assertEquals("nome", pageQuery.getSortFields().get(0).getProperty());
        assertEquals(SortDirection.ASC, pageQuery.getSortFields().get(0).getDirection());
        assertEquals("id", pageQuery.getSortFields().get(1).getProperty());
        assertEquals(SortDirection.DESC, pageQuery.getSortFields().get(1).getDirection());
    }

    @Test
    void toPageableDeveRetornarSemSortQuandoNaoHouverSortFields() {
        PageQuery pageQuery = new PageQuery(1, 20, List.of());

        Pageable pageable = SpringPaginationMapper.toPageable(pageQuery);

        assertEquals(1, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertTrue(pageable.getSort().isUnsorted());
    }

    @Test
    void toPageableDeveAplicarSortAscEDesc() {
        PageQuery pageQuery = new PageQuery(0, 10, List.of(
                new SortField("nome", SortDirection.ASC),
                new SortField("id", SortDirection.DESC)
        ));

        Pageable pageable = SpringPaginationMapper.toPageable(pageQuery);

        Sort.Order nomeOrder = pageable.getSort().getOrderFor("nome");
        Sort.Order idOrder = pageable.getSort().getOrderFor("id");

        assertEquals(Sort.Direction.ASC, nomeOrder.getDirection());
        assertEquals(Sort.Direction.DESC, idOrder.getDirection());
    }

    @Test
    void toPageResultDeveMapearConteudoPaginacaoETotal() {
        Page<String> springPage = new PageImpl<>(
                List.of("a", "b"),
                PageRequest.of(3, 2, Sort.by(Sort.Order.desc("criadoEm"))),
                17
        );

        PageResult<String> pageResult = SpringPaginationMapper.toPageResult(springPage);

        assertEquals(List.of("a", "b"), pageResult.getContent());
        assertEquals(3, pageResult.getPageNumber());
        assertEquals(2, pageResult.getPageSize());
        assertEquals(17, pageResult.getTotalElements());
        assertEquals(1, pageResult.getSortFields().size());
        assertEquals("criadoEm", pageResult.getSortFields().get(0).getProperty());
        assertEquals(SortDirection.DESC, pageResult.getSortFields().get(0).getDirection());
    }

    @Test
    void toSpringPageDevePreservarConteudoPaginacaoETotal() {
        PageResult<String> pageResult = new PageResult<>(
                List.of("x", "y"),
                4,
                5,
                27,
                List.of(new SortField("nome", SortDirection.ASC))
        );

        Page<String> springPage = SpringPaginationMapper.toSpringPage(pageResult);

        assertEquals(List.of("x", "y"), springPage.getContent());
        assertEquals(4, springPage.getNumber());
        assertEquals(5, springPage.getSize());
        assertEquals(27, springPage.getTotalElements());
        assertEquals(Sort.Direction.ASC, springPage.getSort().getOrderFor("nome").getDirection());
    }
}
