package com.pokedex.pokedex_api.controller.dto.response;

import java.util.List;
import java.util.Map;

public record TeamSynergyResponse(
        Map<String, Double> typeCoverage,
        List<String> weaknesses,
        List<String> resistances,
        List<String> immunities,
        PokemonStatsResponse averageStats
) {
}
