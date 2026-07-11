package com.pokedex.pokedex_api.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MiniGameAnswerRequest(@NotBlank(message = "La respuesta es obligatoria") String guess) {
}
