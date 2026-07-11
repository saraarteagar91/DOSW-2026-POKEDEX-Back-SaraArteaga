package com.pokedex.pokedex_api.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DiaryNoteRequest(
        @NotBlank(message = "La nota no puede estar vacía")
        @Size(max = 2000, message = "La nota no puede exceder 2000 caracteres")
        String text
) {
}
