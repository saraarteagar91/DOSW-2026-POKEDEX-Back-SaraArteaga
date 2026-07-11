package com.pokedex.pokedex_api.core.util;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tabla de efectividad de tipos (18 tipos, generación 6+) y cálculo de la sinergia de un equipo (RF-08):
 * cobertura ofensiva, debilidades/resistencias/inmunidades defensivas compartidas y balance de stats.
 *
 * <p>Reglas de agregación usadas (documentadas también en el README):
 * <ul>
 *   <li>Cobertura: para cada tipo defensor, la mejor efectividad que algún tipo propio del equipo logra
 *       atacándolo (STAB).</li>
 *   <li>Debilidad de equipo: tipo atacante cuyo multiplicador defensivo promedio del equipo es mayor a 1.</li>
 *   <li>Resistencia de equipo: promedio menor a 1 (y mayor a 0).</li>
 *   <li>Inmunidad: al menos un miembro del equipo tiene multiplicador 0 ante ese tipo atacante.</li>
 * </ul>
 */
public final class TypeEffectivenessUtil {

    public static final List<String> ALL_TYPES = List.of(
            "Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting", "Poison", "Ground",
            "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
    );

    private static final Map<String, Map<String, Double>> CHART = buildChart();

    private TypeEffectivenessUtil() {
    }

    public static double effectiveness(String attackType, String defendType) {
        return CHART.getOrDefault(attackType, Map.of()).getOrDefault(defendType, 1.0);
    }

    public static double defensiveMultiplier(String attackType, List<String> defenderTypes) {
        double multiplier = 1.0;
        for (String defendType : defenderTypes) {
            multiplier *= effectiveness(attackType, defendType);
        }
        return multiplier;
    }

    public static TeamSynergy computeSynergy(List<Pokemon> members) {
        Map<String, Double> coverage = computeCoverage(members);
        List<String> weaknesses = new ArrayList<>();
        List<String> resistances = new ArrayList<>();
        List<String> immunities = new ArrayList<>();

        for (String attackType : ALL_TYPES) {
            boolean anyImmune = false;
            double sum = 0.0;
            for (Pokemon member : members) {
                double multiplier = defensiveMultiplier(attackType, member.getTypes());
                if (multiplier == 0.0) {
                    anyImmune = true;
                }
                sum += multiplier;
            }
            if (anyImmune) {
                immunities.add(attackType);
            }
            if (members.isEmpty()) {
                continue;
            }
            double average = sum / members.size();
            if (average > 1.0) {
                weaknesses.add(attackType);
            } else if (average < 1.0 && average > 0.0) {
                resistances.add(attackType);
            }
        }

        return new TeamSynergy(coverage, weaknesses, resistances, immunities, averageStats(members));
    }

    private static Map<String, Double> computeCoverage(List<Pokemon> members) {
        Map<String, Double> coverage = new TreeMap<>();
        List<String> ownTypes = members.stream()
                .flatMap(pokemon -> pokemon.getTypes().stream())
                .distinct()
                .toList();
        for (String defendType : ALL_TYPES) {
            double best = ownTypes.stream()
                    .mapToDouble(attackType -> effectiveness(attackType, defendType))
                    .max()
                    .orElse(0.0);
            coverage.put(defendType, best);
        }
        return coverage;
    }

    private static PokemonStats averageStats(List<Pokemon> members) {
        if (members.isEmpty()) {
            return PokemonStats.builder().hp(0).attack(0).defense(0)
                    .specialAttack(0).specialDefense(0).speed(0).build();
        }
        int size = members.size();
        return PokemonStats.builder()
                .hp(average(members, p -> p.getStats().getHp(), size))
                .attack(average(members, p -> p.getStats().getAttack(), size))
                .defense(average(members, p -> p.getStats().getDefense(), size))
                .specialAttack(average(members, p -> p.getStats().getSpecialAttack(), size))
                .specialDefense(average(members, p -> p.getStats().getSpecialDefense(), size))
                .speed(average(members, p -> p.getStats().getSpeed(), size))
                .build();
    }

    private static int average(List<Pokemon> members, java.util.function.ToIntFunction<Pokemon> extractor, int size) {
        int sum = members.stream().mapToInt(extractor).sum();
        return Math.round((float) sum / size);
    }

    private static Map<String, Map<String, Double>> buildChart() {
        Map<String, Map<String, Double>> chart = new LinkedHashMap<>();
        row(chart, "Normal", Map.of("Rock", 0.5, "Ghost", 0.0, "Steel", 0.5));
        row(chart, "Fire", Map.of("Fire", 0.5, "Water", 0.5, "Grass", 2.0, "Ice", 2.0, "Bug", 2.0,
                "Rock", 0.5, "Dragon", 0.5, "Steel", 2.0));
        row(chart, "Water", Map.of("Fire", 2.0, "Water", 0.5, "Grass", 0.5, "Ground", 2.0, "Rock", 2.0,
                "Dragon", 0.5));
        row(chart, "Electric", Map.of("Water", 2.0, "Electric", 0.5, "Grass", 0.5, "Ground", 0.0,
                "Flying", 2.0, "Dragon", 0.5));
        row(chart, "Grass", Map.of("Fire", 0.5, "Water", 2.0, "Grass", 0.5, "Poison", 0.5, "Ground", 2.0,
                "Flying", 0.5, "Bug", 0.5, "Rock", 2.0, "Dragon", 0.5, "Steel", 0.5));
        row(chart, "Ice", Map.of("Fire", 0.5, "Water", 0.5, "Grass", 2.0, "Ice", 0.5, "Ground", 2.0,
                "Flying", 2.0, "Dragon", 2.0, "Steel", 0.5));
        row(chart, "Fighting", Map.ofEntries(
                Map.entry("Normal", 2.0), Map.entry("Ice", 2.0), Map.entry("Poison", 0.5),
                Map.entry("Flying", 0.5), Map.entry("Psychic", 0.5), Map.entry("Bug", 0.5),
                Map.entry("Rock", 2.0), Map.entry("Ghost", 0.0), Map.entry("Dark", 2.0),
                Map.entry("Steel", 2.0), Map.entry("Fairy", 0.5)));
        row(chart, "Poison", Map.of("Grass", 2.0, "Poison", 0.5, "Ground", 0.5, "Rock", 0.5, "Ghost", 0.5,
                "Steel", 0.0, "Fairy", 2.0));
        row(chart, "Ground", Map.of("Fire", 2.0, "Electric", 2.0, "Grass", 0.5, "Poison", 2.0, "Flying", 0.0,
                "Bug", 0.5, "Rock", 2.0, "Steel", 2.0));
        row(chart, "Flying", Map.of("Electric", 0.5, "Grass", 2.0, "Fighting", 2.0, "Bug", 2.0,
                "Rock", 0.5, "Steel", 0.5));
        row(chart, "Psychic", Map.of("Fighting", 2.0, "Poison", 2.0, "Psychic", 0.5, "Dark", 0.0, "Steel", 0.5));
        row(chart, "Bug", Map.of("Fire", 0.5, "Grass", 2.0, "Fighting", 0.5, "Poison", 0.5, "Flying", 0.5,
                "Psychic", 2.0, "Ghost", 0.5, "Dark", 2.0, "Steel", 0.5, "Fairy", 0.5));
        row(chart, "Rock", Map.of("Fire", 2.0, "Ice", 2.0, "Fighting", 0.5, "Ground", 0.5, "Flying", 2.0,
                "Bug", 2.0, "Steel", 0.5));
        row(chart, "Ghost", Map.of("Normal", 0.0, "Psychic", 2.0, "Ghost", 2.0, "Dark", 0.5));
        row(chart, "Dragon", Map.of("Dragon", 2.0, "Steel", 0.5, "Fairy", 0.0));
        row(chart, "Dark", Map.of("Fighting", 0.5, "Psychic", 2.0, "Ghost", 2.0, "Dark", 0.5, "Fairy", 0.5));
        row(chart, "Steel", Map.of("Fire", 0.5, "Water", 0.5, "Electric", 0.5, "Ice", 2.0, "Rock", 2.0,
                "Steel", 0.5, "Fairy", 2.0));
        row(chart, "Fairy", Map.of("Fire", 0.5, "Fighting", 2.0, "Poison", 0.5, "Dragon", 2.0,
                "Dark", 2.0, "Steel", 0.5));
        return chart;
    }

    private static void row(Map<String, Map<String, Double>> chart, String attackType,
                             Map<String, Double> effectivenessByDefendType) {
        chart.put(attackType, effectivenessByDefendType);
    }
}
