package com.fiap.gestao_servicos.core.domain;

import java.util.Objects;

public class Cnpj {

    private final String value;

    public Cnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo ou vazio");
        }
        this.value = cnpj.trim();

        if (!hasValidLength()) {
            throw new IllegalArgumentException("CNPJ deve conter 14 dígitos");
        }
        if (!isValid()) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }

    private String getCleanValue() {
        return value.replaceAll("\\D", "");
    }

    public boolean hasValidLength() {
        return getCleanValue().length() == 14;
    }

    public boolean isValid() {
        String clean = getCleanValue();
        if (clean.length() != 14) {
            return false;
        }
        if (isAllDigitsSame(clean)) {
            return false;
        }

        int firstCheckDigit = calculateCheckDigit(clean, 12, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        if (firstCheckDigit != (clean.charAt(12) - '0')) {
            return false;
        }

        int secondCheckDigit = calculateCheckDigit(clean, 13, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        return secondCheckDigit == (clean.charAt(13) - '0');
    }

    private boolean isAllDigitsSame(String clean) {
        return clean.matches("(\\d)\\1{13}");
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
            throw new IllegalStateException("CNPJ inválido não pode ser mascarado");
        }
        String clean = getCleanValue();
        return String.format("%s.%s.%s/%s-%s",
                clean.substring(0, 2),
                clean.substring(2, 5),
                clean.substring(5, 8),
                clean.substring(8, 12),
                clean.substring(12));
    }

    public boolean hasValidMask() {
        return value.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Cnpj other)) {
            return false;
        }
        return Objects.equals(getCleanValue(), other.getCleanValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCleanValue());
    }

    @Override
    public String toString() {
        return getMasked();
    }
}


