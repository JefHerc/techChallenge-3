package com.fiap.gestao_servicos.infrastructure.mapper;

import java.text.Normalizer;
import java.time.DayOfWeek;
import java.util.Locale;

public final class DiaSemanaMapper {

    private DiaSemanaMapper() {
    }

    public static DayOfWeek parse(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String normalizado = normalizar(valor);

        return switch (normalizado) {
            case "segunda", "segunda-feira", "monday" -> DayOfWeek.MONDAY;
            case "terca", "terca-feira", "tuesday" -> DayOfWeek.TUESDAY;
            case "quarta", "quarta-feira", "wednesday" -> DayOfWeek.WEDNESDAY;
            case "quinta", "quinta-feira", "thursday" -> DayOfWeek.THURSDAY;
            case "sexta", "sexta-feira", "friday" -> DayOfWeek.FRIDAY;
            case "sabado", "sabado-feira", "saturday" -> DayOfWeek.SATURDAY;
            case "domingo", "sunday" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException(
                    "Dia da semana invalido. Use segunda-feira, terca-feira, quarta-feira, quinta-feira, sexta-feira, sabado ou domingo.");
        };
    }

    public static String toPtBr(DayOfWeek diaSemana) {
        if (diaSemana == null) {
            return null;
        }

        return switch (diaSemana) {
            case MONDAY -> "segunda-feira";
            case TUESDAY -> "terca-feira";
            case WEDNESDAY -> "quarta-feira";
            case THURSDAY -> "quinta-feira";
            case FRIDAY -> "sexta-feira";
            case SATURDAY -> "sabado";
            case SUNDAY -> "domingo";
        };
    }

    private static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return semAcento
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replaceAll("\\s+", "-");
    }
}
