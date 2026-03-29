package com.fiap.gestao_servicos.core.pagination;

public class SortField {

    private final String property;
    private final SortDirection direction;

    public SortField(String property, SortDirection direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public SortDirection getDirection() {
        return direction;
    }
}