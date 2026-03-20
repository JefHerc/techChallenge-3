package com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento;

import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.estabelecimento.EstabelecimentoResponseDto;
import com.fiap.gestao_servicos.infrastructure.mapper.profissional.ProfissionalDomainToResponseMapper;
import com.fiap.gestao_servicos.infrastructure.mapper.servico.ServicoDomainToResponseMapper;

public class EstabelecimentoDomainToResponseMapper {

    public static EstabelecimentoResponseDto toResponse(Estabelecimento estabelecimento) {
        if (estabelecimento == null) {
            return null;
        }

        EstabelecimentoResponseDto response = new EstabelecimentoResponseDto();
        response.setId(estabelecimento.getId());
        response.setNome(estabelecimento.getNome());
        response.setEndereco(toEnderecoDto(estabelecimento.getEndereco()));
        response.setProfissionais(estabelecimento.getProfissionais().stream()
                .map(ProfissionalDomainToResponseMapper::toResponse)
                .toList());
        response.setServicos(estabelecimento.getServicos().stream()
                .map(ServicoDomainToResponseMapper::toResponse)
                .toList());
        response.setCnpj(estabelecimento.getCnpj() != null ? estabelecimento.getCnpj().getValue() : null);
        response.setUrlFotos(estabelecimento.getUrlFotos());
        response.setHorarioFuncionamento(estabelecimento.getHorarioFuncionamento());
        return response;
    }

    private static EnderecoDto toEnderecoDto(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        EnderecoDto dto = new EnderecoDto();
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        return dto;
    }
}