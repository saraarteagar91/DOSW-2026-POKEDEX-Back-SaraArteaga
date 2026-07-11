package com.pokedex.pokedex_api.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/** Formato estándar de error de toda la API (doc §12.1). */
public record ApiError(
        int status,
        String errorCode,
        String message,
        String path,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {
    }
}
