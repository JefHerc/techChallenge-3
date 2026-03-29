package com.fiap.gestao_servicos.infrastructure.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public final class PageUtils {

    private PageUtils() {
    }

    public static <T> Page<T> paginate(List<T> items, Pageable pageable) {
        if (items == null || items.isEmpty()) {
            return Page.empty(pageable);
        }

        int start = Math.toIntExact(pageable.getOffset());
        if (start >= items.size()) {
            return new PageImpl<>(List.of(), pageable, items.size());
        }

        int end = Math.min(start + pageable.getPageSize(), items.size());
        return new PageImpl<>(items.subList(start, end), pageable, items.size());
    }
}