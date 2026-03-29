package com.fiap.gestao_servicos.core.domain;

import java.util.List;

public class Estabelecimento {

    private final Long id;
    private final String nome;
    private final Endereco endereco;
    private final List<Profissional> profissionais;
    private final List<Servico> servicos;
    private final Cnpj cnpj;
    private final List<String> urlFotos;
    private final List<HorarioFuncionamento> horarioFuncionamento;

    public Estabelecimento(Long id, String nome, Endereco endereco, List<Profissional> profissionais, List<Servico> servicos, Cnpj cnpj, List<String> urlFotos, List<HorarioFuncionamento> horarioFuncionamento) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.id = id;
        this.nome = nome.trim();

        this.endereco = endereco;

        this.profissionais = profissionais != null ? List.copyOf(profissionais) : List.of();

        this.servicos = servicos != null ? List.copyOf(servicos) : List.of();

        this.cnpj = cnpj;

        this.urlFotos = urlFotos != null ? List.copyOf(urlFotos) : List.of();

        this.horarioFuncionamento = horarioFuncionamento != null ? List.copyOf(horarioFuncionamento) : List.of();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public List<Profissional> getProfissionais() {
        return profissionais;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public List<String> getUrlFotos() {
        return urlFotos;
    }

    public List<HorarioFuncionamento> getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    @Override
    public String toString() {
        return String.format("Estabelecimento: %s, Endereço: %s, CNPJ: %s, Horário de Funcionamento: %s, Profissionais: %d, Serviços: %d, Fotos: %d",
                nome, endereco != null ? endereco.toString() : "N/A", cnpj != null ? cnpj.getMasked() : "N/A", horarioFuncionamento != null ? horarioFuncionamento.toString() : "N/A", profissionais.size(), servicos.size(), urlFotos.size());
    }
}


