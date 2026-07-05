package com.pokedex.pokedex_api;

public record PokemonResponse(
        Long id,
        Integer pokedexNumber,
        String name,
        String imageUrl,
        Integer generation
) {}