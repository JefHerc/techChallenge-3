package com.fiap.gestao_servicos.core.domain;

public class Cpf {

    private final String value;

    public Cpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }

        this.value = cpf.trim();

        if (!hasValidLength()) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
        if (!isValid()) {
            throw new IllegalArgumentException("CPF inválido");
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

        int firstCheckDigit = calculateCheckDigit(clean, 9, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
        if (firstCheckDigit != (clean.charAt(9) - '0')) {
            return false;
        }

        int secondCheckDigit = calculateCheckDigit(clean, 10, new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2});
        return secondCheckDigit == (clean.charAt(10) - '0');
    }

    private boolean isAllDigitsSame(String clean) {
        return clean.matches("(\\d)\\1{10}");
    }

    private int calculateCheckDigit(String clean, int length, int[] weights) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += (clean.charAt(i) - '0') * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    public String getMasked() {
        if (!isValid()) {
            throw new IllegalStateException("CPF inválido não pode ser mascarado");
        }
        String clean = getCleanValue();
        return String.format("%s.%s.%s-%s",
                clean.substring(0, 3),
                clean.substring(3, 6),
                clean.substring(6, 9),
                clean.substring(9));
    }

    public boolean hasValidMask() {
        return value.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return getMasked();
    }
}
