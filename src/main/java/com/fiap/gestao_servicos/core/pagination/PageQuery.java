package com.fiap.gestao_servicos.core.pagination;

import java.util.List;

public class PageQuery {

    private final int pageNumber;
    private final int pageSize;
    private final List<SortField> sortFields;

    public PageQuery(int pageNumber, int pageSize, List<SortField> sortFields) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortFields = sortFields == null ? List.of() : List.copyOf(sortFields);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<SortField> getSortFields() {
        return sortFields;
    }
}