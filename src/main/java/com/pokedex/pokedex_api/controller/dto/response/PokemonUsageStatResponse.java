package com.pokedex.pokedex_api.controller.dto.response;

public record PokemonUsageStatResponse(
        Long pokemonId, Integer nationalNumber, String name, long viewCount, long choiceCount
) {
}
