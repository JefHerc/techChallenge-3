package com.fiap.gestao_servicos.core.domain;

public class Celular {

    private final String value;

    public Celular(String celular) {
        if (celular == null || celular.trim().isEmpty()) {
            throw new IllegalArgumentException("Celular não pode ser nulo ou vazio");
        }

        this.value = celular.trim();

        if (!hasValidLength()) {
            throw new IllegalArgumentException("Celular deve conter 11 dígitos");
        }
        if (!isValid()) {
            throw new IllegalArgumentException("Celular inválido");
        }
    }

    private String getCleanValue() {
        return value.replaceAll("\\D", "");
    }

    public boolean hasValidLength() {
        return getCleanValue().length() == 11;
    }

    public boolean isValid() {
        String clean = getCleanValue();
        if (clean.length() != 11) {
            return false;
        }
        if (isAllDigitsSame(clean)) {
            return false;
        }
        int ddd = Integer.parseInt(clean.substring(0, 2));
        if (ddd < 11 || ddd > 99) {
            return false;
        }
        if (clean.charAt(2) != '9') {
            return false;
        }
        return true;
    }

    private boolean isAllDigitsSame(String clean) {
        return clean.matches("(\\d)\\1{10}");
    }

    public String getMasked() {
        if (!isValid()) {
            throw new IllegalStateException("Celular inválido não pode ser mascarado");
        }
        String clean = getCleanValue();
        return String.format("(%s) %s-%s",
                clean.substring(0, 2),
                clean.substring(2, 7),
                clean.substring(7));
    }

    public boolean hasValidMask() {
        return value.matches("\\(\\d{2}\\) \\d{5}-\\d{4}");
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return getMasked();
    }
}
