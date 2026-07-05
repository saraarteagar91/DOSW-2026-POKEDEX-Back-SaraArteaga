package com.pokedex.pokedex_api;
import jakarta.validation.constraints.*;
import java.util.List;

public record PokemonRequest(
        @NotNull(message = "El número de Pokédex es obligatorio")
        @Min(value = 1, message = "El número debe ser mayor a 0")
        Integer pokedexNumber,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 carácteres")
        String name,
        String imageUrl,
        Integer generation
) {}