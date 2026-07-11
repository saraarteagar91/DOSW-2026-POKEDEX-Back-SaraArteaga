package com.pokedex.pokedex_api.core.model;

public record PokemonUsageStat(
        Long pokemonId,
        Integer nationalNumber,
        String name,
        long viewCount,
        long choiceCount
) {
}
