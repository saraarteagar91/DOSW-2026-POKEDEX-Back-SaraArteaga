package com.pokedex.pokedex_api.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/** RF-11/RF-12: el número de Pokédex se recibe solo al crear; en la edición se ignora (RN-06). */
public record PokemonRequest(
        @NotNull(message = "El número de Pokédex es obligatorio")
        @Min(value = 1, message = "El número debe ser mayor a 0")
        Integer nationalNumber,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description,

        @NotBlank(message = "La imagen es obligatoria")
        String imageUrl,

        @NotEmpty(message = "Debe tener al menos un tipo")
        @Size(max = 2, message = "Un Pokémon puede tener máximo 2 tipos")
        List<String> types,

        @NotBlank(message = "La región es obligatoria")
        String region,

        @NotNull @Min(1) Integer generation,

        Boolean hasMega,

        @Min(1) Integer evolutionStage,

        Long evolvesFromId,

        String color,

        List<String> abilities,

        List<String> moves,

        @NotNull @Valid PokemonStatsRequest stats
) {
}
