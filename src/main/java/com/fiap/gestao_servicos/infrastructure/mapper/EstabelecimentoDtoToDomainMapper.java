package com.fiap.gestao_servicos.infrastructure.mapper;

import com.fiap.gestao_servicos.core.domain.*;
import com.fiap.gestao_servicos.infrastructure.controller.EnderecoDto;
import com.fiap.gestao_servicos.infrastructure.controller.EstabelecimentoDto;
import com.fiap.gestao_servicos.infrastructure.controller.ProfissionalDto;
import com.fiap.gestao_servicos.infrastructure.controller.ServicoDto;

import java.util.stream.Collectors;

public class EstabelecimentoDtoToDomainMapper {

    public static Estabelecimento toDomain(EstabelecimentoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Estabelecimento(
            null,
            dto.getNome(),
            toEndereco(dto.getEndereco()),
            dto.getProfissionais() != null ? dto.getProfissionais().stream().map(EstabelecimentoDtoToDomainMapper::toProfissional).collect(Collectors.toList()) : null,
            dto.getServicos() != null ? dto.getServicos().stream().map(EstabelecimentoDtoToDomainMapper::toServico).collect(Collectors.toList()) : null,
            new Cnpj(dto.getCnpj()),
            dto.getUrlFotos(),
            dto.getHorarioFuncionamento()
        );
    }

    private static Endereco toEndereco(EnderecoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Endereco(
            dto.getLogradouro(),
            dto.getNumero(),
            dto.getComplemento(),
            dto.getBairro(),
            dto.getCidade(),
            dto.getEstado(),
            dto.getCep()
        );
    }

    private static Profissional toProfissional(ProfissionalDto dto) {
        if (dto == null) {
            return null;
        }

        return new Profissional(
            dto.getNome(),
            new Cpf(dto.getCpf()),
            new Celular(dto.getCelular()),
            new Email(dto.getEmail()),
            dto.getUrlFoto(),
            dto.getDescricao()
        );
    }

    private static Servico toServico(ServicoDto dto) {
        if (dto == null) {
            return null;
        }

        return new Servico(
            dto.getNome(),
            dto.getDuracaoMedia()
        );
    }
}
