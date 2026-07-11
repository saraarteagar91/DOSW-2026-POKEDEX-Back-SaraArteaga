package com.pokedex.pokedex_api.core.model;

/**
 * Combinación de criterios para RF-02 (búsqueda) y RF-04 (filtros). Todos los campos son opcionales.
 */
public record PokemonFilterCriteria(
        String search,
        String region,
        String type,
        Integer generation,
        String ability,
        String move,
        Boolean hasMega,
        String color,
        Integer evolutionStage,
        Integer minTotalStats,
        Integer maxTotalStats
) {
}
