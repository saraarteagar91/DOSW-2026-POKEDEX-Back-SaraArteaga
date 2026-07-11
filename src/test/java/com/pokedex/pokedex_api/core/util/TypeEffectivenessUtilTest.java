package com.pokedex.pokedex_api.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import java.util.List;
import org.junit.jupiter.api.Test;

class TypeEffectivenessUtilTest {

    @Test
    void effectiveness_waterVsFire_isSuperEffective() {
        assertThat(TypeEffectivenessUtil.effectiveness("Water", "Fire")).isEqualTo(2.0);
    }

    @Test
    void effectiveness_electricVsGround_isNoEffect() {
        assertThat(TypeEffectivenessUtil.effectiveness("Electric", "Ground")).isEqualTo(0.0);
    }

    @Test
    void effectiveness_unknownPair_defaultsToNeutral() {
        assertThat(TypeEffectivenessUtil.effectiveness("Normal", "Fighting")).isEqualTo(1.0);
    }

    @Test
    void defensiveMultiplier_dualType_multipliesBothTypes() {
        double multiplier = TypeEffectivenessUtil.defensiveMultiplier("Ground", List.of("Grass", "Poison"));
        assertThat(multiplier).isEqualTo(1.0); // 0.5 (Grass) * 2.0 (Poison)
    }

    @Test
    void computeSynergy_emptyTeam_returnsNeutralValues() {
        TeamSynergy synergy = TypeEffectivenessUtil.computeSynergy(List.of());
        assertThat(synergy.weaknesses()).isEmpty();
        assertThat(synergy.immunities()).isEmpty();
    }

    @Test
    void computeSynergy_grassFireWaterTeam_flagsSharedWeaknesses() {
        Pokemon grass = pokemon(List.of("Grass"));
        Pokemon fire = pokemon(List.of("Fire"));
        Pokemon water = pokemon(List.of("Water"));

        TeamSynergy synergy = TypeEffectivenessUtil.computeSynergy(List.of(grass, fire, water));

        assertThat(synergy.averageStats().getTotal()).isPositive();
        assertThat(synergy.typeCoverage()).containsKeys(TypeEffectivenessUtil.ALL_TYPES.toArray(new String[0]));
    }

    private Pokemon pokemon(List<String> types) {
        return Pokemon.builder()
                .id(1L).name("Test").types(types)
                .stats(PokemonStats.builder().hp(50).attack(50).defense(50)
                        .specialAttack(50).specialDefense(50).speed(50).build())
                .build();
    }
}
