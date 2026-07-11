package com.pokedex.pokedex_api.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.CommunityStats;
import com.pokedex.pokedex_api.core.model.PokemonUsageStat;
import java.util.List;
import org.junit.jupiter.api.Test;

class StatsDtoMapperTest {

    private final StatsDtoMapper mapper = new StatsDtoMapper();

    @Test
    void toResponse_mapsAllFields() {
        CommunityStats stats = new CommunityStats(
                List.of(new PokemonUsageStat(1L, 1, "Bulbasaur", 10L, 2L)),
                List.of(new PokemonUsageStat(25L, 25, "Pikachu", 3L, 7L)),
                13L, 5L);

        var response = mapper.toResponse(stats);

        assertThat(response.totalViews()).isEqualTo(13L);
        assertThat(response.totalTeamsCreated()).isEqualTo(5L);
        assertThat(response.mostViewed().get(0).name()).isEqualTo("Bulbasaur");
        assertThat(response.mostChosenInTeams().get(0).name()).isEqualTo("Pikachu");
    }
}
