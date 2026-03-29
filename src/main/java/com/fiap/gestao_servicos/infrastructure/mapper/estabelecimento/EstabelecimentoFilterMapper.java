package com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento;

import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoFilterDto;

public final class EstabelecimentoFilterMapper {

    private EstabelecimentoFilterMapper() {
    }

    public static EstabelecimentoFilter toDomain(EstabelecimentoFilterDto dto) {
        if (dto == null) {
            return new EstabelecimentoFilter();
        }

        EstabelecimentoFilter filter = new EstabelecimentoFilter();
        filter.setNome(dto.getNome());
        filter.setCidade(dto.getCidade());
        filter.setEstado(dto.getEstado());
        filter.setBairro(dto.getBairro());
        filter.setServicoNome(dto.getServicoNome());
        filter.setNotaMinima(dto.getNotaMinima());
        filter.setPrecoMin(dto.getPrecoMin());
        filter.setPrecoMax(dto.getPrecoMax());
        filter.setDataDisponivel(dto.getDataDisponivel());
        filter.setProfissionalNotaMinima(dto.getProfissionalNotaMinima());
        return filter;
    }
}