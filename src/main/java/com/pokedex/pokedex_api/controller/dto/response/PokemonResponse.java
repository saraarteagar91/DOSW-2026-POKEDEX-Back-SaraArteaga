package com.pokedex.pokedex_api.controller.dto.response;

import java.util.List;

public record PokemonResponse(
        Long id,
        Integer nationalNumber,
        String name,
        String description,
        String imageUrl,
        List<String> types,
        String region,
        Integer generation,
        Boolean hasMega,
        Integer evolutionStage,
        Long evolvesFromId,
        String color,
        List<String> abilities,
        List<String> moves,
        PokemonStatsResponse stats
) {
}
