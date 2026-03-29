package com.fiap.gestao_servicos.infrastructure.pagination;

import com.fiap.gestao_servicos.core.pagination.PageQuery;
import com.fiap.gestao_servicos.core.pagination.PageResult;
import com.fiap.gestao_servicos.core.pagination.SortDirection;
import com.fiap.gestao_servicos.core.pagination.SortField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public final class SpringPaginationMapper {

    private SpringPaginationMapper() {
    }

    public static PageQuery toPageQuery(Pageable pageable) {
        return new PageQuery(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().stream()
                        .map(order -> new SortField(order.getProperty(), order.isAscending() ? SortDirection.ASC : SortDirection.DESC))
                        .toList()
        );
    }

    public static Pageable toPageable(PageQuery pageQuery) {
        Sort sort = pageQuery.getSortFields().isEmpty()
                ? Sort.unsorted()
                : Sort.by(pageQuery.getSortFields().stream()
                        .map(field -> new Sort.Order(toDirection(field.getDirection()), field.getProperty()))
                        .toList());
        return PageRequest.of(pageQuery.getPageNumber(), pageQuery.getPageSize(), sort);
    }

    public static <T> PageResult<T> toPageResult(Page<T> page) {
        List<SortField> sortFields = page.getSort().stream()
                .map(order -> new SortField(order.getProperty(), order.isAscending() ? SortDirection.ASC : SortDirection.DESC))
                .toList();
        return new PageResult<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), sortFields);
    }

    public static <T> Page<T> toSpringPage(PageResult<T> pageResult) {
        return new PageImpl<>(pageResult.getContent(), toPageable(new PageQuery(
                pageResult.getPageNumber(),
                pageResult.getPageSize(),
                pageResult.getSortFields())), pageResult.getTotalElements());
    }

    private static Sort.Direction toDirection(SortDirection sortDirection) {
        return sortDirection == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}