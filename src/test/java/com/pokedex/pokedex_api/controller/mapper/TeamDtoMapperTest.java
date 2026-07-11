package com.pokedex.pokedex_api.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TeamDtoMapperTest {

    private final TeamDtoMapper mapper = new TeamDtoMapper(new PokemonDtoMapperImpl());

    @Test
    void toResponse_mapsTeamFields() {
        Team team = Team.builder().id(1L).userId(10L).name("Iniciales").pokemonIds(List.of(1L, 4L, 7L)).build();
        var response = mapper.toResponse(team);
        assertThat(response.name()).isEqualTo("Iniciales");
        assertThat(response.pokemonIds()).containsExactly(1L, 4L, 7L);
    }

    @Test
    void toResponse_mapsSynergyIncludingAverageStats() {
        TeamSynergy synergy = new TeamSynergy(
                Map.of("Fire", 2.0),
                List.of("Water"),
                List.of("Grass"),
                List.of(),
                PokemonStats.builder().hp(50).attack(50).defense(50)
                        .specialAttack(50).specialDefense(50).speed(50).build());

        var response = mapper.toResponse(synergy);

        assertThat(response.weaknesses()).containsExactly("Water");
        assertThat(response.averageStats().total()).isEqualTo(300);
    }
}
