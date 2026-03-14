package com.fiap.gestao_servicos.core.domain;

public class Endereco {

    private final String logradouro;
    private final String numero;
    private final String complemento;
    private final String bairro;
    private final String cidade;
    private final String estado;
    private final String cep;

    public Endereco(String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep) {
        if (logradouro == null || logradouro.trim().isEmpty()) {
            throw new IllegalArgumentException("Logradouro não pode ser nulo ou vazio");
        }
        this.logradouro = logradouro.trim();

        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Número não pode ser nulo ou vazio");
        }
        this.numero = numero.trim();

        this.complemento = complemento != null ? complemento.trim() : null;

        if (bairro == null || bairro.trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro não pode ser nulo ou vazio");
        }
        this.bairro = bairro.trim();

        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade não pode ser nula ou vazia");
        }
        this.cidade = cidade.trim();

        if (estado == null || !estado.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("Estado deve ser uma sigla de 2 letras maiúsculas");
        }
        this.estado = estado;

        if (cep == null || !cep.replaceAll("\\D", "").matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos numéricos");
        }
        this.cep = cep.replaceAll("\\D", "");
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    public String getCepMasked() {
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }

    public String toString() {
        return String.format("%s, %s%s, %s - %s, CEP: %s",
                logradouro, numero, complemento != null ? " " + complemento : "", bairro, cidade, getCepMasked());
    }
}
