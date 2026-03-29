package com.fiap.gestao_servicos.core.pagination;

import java.util.List;
import java.util.function.Function;

public class PageResult<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final List<SortField> sortFields;

    public PageResult(List<T> content, int pageNumber, int pageSize, long totalElements, List<SortField> sortFields) {
        this.content = content == null ? List.of() : List.copyOf(content);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.sortFields = sortFields == null ? List.of() : List.copyOf(sortFields);
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<SortField> getSortFields() {
        return sortFields;
    }

    public int getNumberOfElements() {
        return content.size();
    }

    public int getTotalPages() {
        if (pageSize <= 0) {
            return totalElements > 0 ? 1 : 0;
        }
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    public <R> PageResult<R> map(Function<T, R> mapper) {
        return new PageResult<>(content.stream().map(mapper).toList(), pageNumber, pageSize, totalElements, sortFields);
    }
}