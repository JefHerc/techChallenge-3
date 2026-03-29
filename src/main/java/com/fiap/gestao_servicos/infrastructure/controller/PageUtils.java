package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.infrastructure.pagination.SpringPaginationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public final class PageUtils {

    private PageUtils() {
    }

    public static PageQuery toPageQuery(Pageable pageable) {
        return SpringPaginationMapper.toPageQuery(pageable);
    }

    public static <T> Page<T> toSpringPage(PageResult<T> pageResult) {
        return SpringPaginationMapper.toSpringPage(pageResult);
    }
}