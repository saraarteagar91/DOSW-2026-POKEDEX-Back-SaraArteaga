package com.pokedex.pokedex_api.controller.dto.response;

import java.util.List;

public record StatsResponse(
        List<PokemonUsageStatResponse> mostViewed,
        List<PokemonUsageStatResponse> mostChosenInTeams,
        long totalViews,
        long totalTeamsCreated
) {
}
