package com.pokedex.pokedex_api.core.model;

import java.util.List;

/**
 * Panel de estadísticas de la comunidad (RF-10, RN-08).
 */
public record CommunityStats(
        List<PokemonUsageStat> mostViewed,
        List<PokemonUsageStat> mostChosenInTeams,
        long totalViews,
        long totalTeamsCreated
) {
}
