package com.pokedex.pokedex_api.core.model;

import java.util.List;
import java.util.Map;

/**
 * Análisis de sinergia calculado en TypeEffectivenessUtil (RF-08): cobertura ofensiva del equipo,
 * debilidades/resistencias/inmunidades defensivas combinadas, y balance promedio de estadísticas.
 */
public record TeamSynergy(
        Map<String, Double> typeCoverage,
        List<String> weaknesses,
        List<String> resistances,
        List<String> immunities,
        PokemonStats averageStats
) {
}
