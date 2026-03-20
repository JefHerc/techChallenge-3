package com.fiap.gestao_servicos.core.domain;

public class Avaliacao {

    private Long id;
    private Agendamento agendamento;
    private double notaEstabelecimento;
    private double notaProfissional;
    private String comentarioEstabelecimento;
    private String comentarioProfissional;

    public Avaliacao(Long id, Agendamento agendamento, double notaEstabelecimento, double notaProfissional,
            String comentarioEstabelecimento, String comentarioProfissional) {
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não pode ser nulo");
        }
        if (notaEstabelecimento < 0 || notaEstabelecimento > 5) {
            throw new IllegalArgumentException("Nota do estabelecimento deve ser entre 0 e 5");
        }
        if (notaProfissional < 0 || notaProfissional > 5) {
            throw new IllegalArgumentException("Nota do profissional deve ser entre 0 e 5");
        }
        if (!comentarioEstabelecimento.isEmpty() && comentarioEstabelecimento.length() > 500) {
            throw new IllegalArgumentException("Comentário do estabelecimento não pode ter mais de 500 caracteres");
        }
        if (!comentarioProfissional.isEmpty() && comentarioProfissional.length() > 500) {
            throw new IllegalArgumentException("Comentário do profissional não pode ter mais de 500 caracteres");
        }

        this.id = id;
        this.agendamento = agendamento;
        this.notaEstabelecimento = notaEstabelecimento;
        this.notaProfissional = notaProfissional;
        this.comentarioEstabelecimento = comentarioEstabelecimento;
        this.comentarioProfissional = comentarioProfissional;
    }

    public Long getId() {
        return id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public double getNotaEstabelecimento() {
        return notaEstabelecimento;
    }

    public double getNotaProfissional() {
        return notaProfissional;
    }

    public String getComentarioEstabelecimento() {
        return comentarioEstabelecimento;
    }

    public String getComentarioProfissional() {
        return comentarioProfissional;
    }
}
