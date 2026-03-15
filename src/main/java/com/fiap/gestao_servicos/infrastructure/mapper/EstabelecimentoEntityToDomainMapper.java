package com.fiap.gestao_servicos.infrastructure.mapper;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.infrastructure.persistence.EnderecoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.ServicoEntity;

import java.util.stream.Collectors;

public class EstabelecimentoEntityToDomainMapper {

    public static Estabelecimento toDomain(EstabelecimentoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Estabelecimento(
                entity.getId(),
                entity.getNome(),
                toEndereco(entity.getEndereco()),
                entity.getProfissionais() != null ? entity.getProfissionais().stream().map(EstabelecimentoEntityToDomainMapper::toProfissional).collect(Collectors.toList()) : null,
                entity.getServicos() != null ? entity.getServicos().stream().map(EstabelecimentoEntityToDomainMapper::toServico).collect(Collectors.toList()) : null,
                entity.getCnpj() != null ? new Cnpj(entity.getCnpj()) : null,
                entity.getUrlFotos(),
                entity.getHorarioFuncionamento()
        );
    }

    private static Endereco toEndereco(EnderecoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Endereco(
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep()
        );
    }

    private static Profissional toProfissional(ProfissionalEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Profissional(
                entity.getNome(),
                entity.getCpf() != null ? new Cpf(entity.getCpf()) : null,
                entity.getCelular() != null ? new Celular(entity.getCelular()) : null,
                entity.getEmail() != null ? new Email(entity.getEmail()) : null,
                entity.getUrlFoto(),
                entity.getDescricao()
        );
    }

    private static Servico toServico(ServicoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Servico(
                entity.getNome(),
                entity.getDuracaoMedia()
        );
    }
}