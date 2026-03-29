package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.pagination.SortDirection;
import com.fiap.gestao_servicos.core.pagination.SortField;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageUtilsTest {

    @Test
    void toPageQueryDeveDelegarConversaoCorretamente() {
        Pageable pageable = PageRequest.of(1, 25, Sort.by(Sort.Order.desc("id")));

        PageQuery query = PageUtils.toPageQuery(pageable);

        assertEquals(1, query.getPageNumber());
        assertEquals(25, query.getPageSize());
        assertEquals(1, query.getSortFields().size());
        assertEquals("id", query.getSortFields().get(0).getProperty());
        assertEquals(SortDirection.DESC, query.getSortFields().get(0).getDirection());
    }

    @Test
    void toSpringPageDeveDelegarConversaoCorretamente() {
        PageResult<String> pageResult = new PageResult<>(
                List.of("item-1", "item-2"),
                0,
                2,
                8,
                List.of(new SortField("nome", SortDirection.ASC))
        );

        Page<String> springPage = PageUtils.toSpringPage(pageResult);

        assertEquals(List.of("item-1", "item-2"), springPage.getContent());
        assertEquals(0, springPage.getNumber());
        assertEquals(2, springPage.getSize());
        assertEquals(8, springPage.getTotalElements());
        assertEquals("nome", springPage.getSort().getOrderFor("nome").getProperty());
    }
}
