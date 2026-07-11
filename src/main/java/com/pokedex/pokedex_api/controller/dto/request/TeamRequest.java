package com.pokedex.pokedex_api.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record TeamRequest(
        @NotBlank(message = "El equipo debe tener un nombre") String name,
        @NotEmpty(message = "El equipo debe tener al menos un Pokémon")
        @Size(max = 6, message = "Un equipo puede tener máximo 6 Pokémon")
        List<Long> pokemonIds
) {
}
