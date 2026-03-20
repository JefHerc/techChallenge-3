package com.fiap.gestao_servicos.infrastructure.mapper.estabelecimento;

import com.fiap.gestao_servicos.core.domain.*;
import com.fiap.gestao_servicos.infrastructure.persistence.*;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;

import java.util.stream.Collectors;

public class EstabelecimentoDomainToEntityMapper {

    public static EstabelecimentoEntity toEntity(Estabelecimento domain) {
        if (domain == null) {
            return null;
        }

        EstabelecimentoEntity entity = new EstabelecimentoEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEndereco(toEnderecoEntity(domain.getEndereco()));
        entity.setProfissionais(domain.getProfissionais() != null ? domain.getProfissionais().stream().map(EstabelecimentoDomainToEntityMapper::toProfissionalEntity).collect(Collectors.toList()) : null);
        entity.setServicos(domain.getServicos() != null ? domain.getServicos().stream().map(EstabelecimentoDomainToEntityMapper::toServicoEntity).collect(Collectors.toList()) : null);
        entity.setCnpj(domain.getCnpj() != null ? domain.getCnpj().getValue() : null);
        entity.setUrlFotos(domain.getUrlFotos());
        entity.setHorarioFuncionamento(domain.getHorarioFuncionamento());

        return entity;
    }

    private static EnderecoEntity toEnderecoEntity(Endereco domain) {
        if (domain == null) {
            return null;
        }

        EnderecoEntity entity = new EnderecoEntity();
        entity.setLogradouro(domain.getLogradouro());
        entity.setNumero(domain.getNumero());
        entity.setComplemento(domain.getComplemento());
        entity.setBairro(domain.getBairro());
        entity.setCidade(domain.getCidade());
        entity.setEstado(domain.getEstado());
        entity.setCep(domain.getCep());

        return entity;
    }

    private static ProfissionalEntity toProfissionalEntity(Profissional domain) {
        if (domain == null) {
            return null;
        }

        ProfissionalEntity entity = new ProfissionalEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCpf(domain.getCpf() != null ? domain.getCpf().getValue() : null);
        entity.setCelular(domain.getCelular() != null ? domain.getCelular().getValue() : null);
        entity.setEmail(domain.getEmail() != null ? domain.getEmail().getNormalized() : null);
        entity.setUrlFoto(domain.getUrlFoto());
        entity.setDescricao(domain.getDescricao());

        return entity;
    }

    private static ServicoEntity toServicoEntity(Servico domain) {
        if (domain == null) {
            return null;
        }

        ServicoEntity entity = new ServicoEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDuracaoMedia(domain.getDuracaoMedia());

        return entity;
    }
}