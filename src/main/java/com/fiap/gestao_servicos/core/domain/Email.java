package com.fiap.gestao_servicos.core.domain;

public class Email {

    private final String value;

    public Email(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }

        this.value = email.trim();

        if (!isValid()) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    public boolean isValid() {
        return value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public String getNormalized() {
        return value.toLowerCase();
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return getNormalized();
    }
}
