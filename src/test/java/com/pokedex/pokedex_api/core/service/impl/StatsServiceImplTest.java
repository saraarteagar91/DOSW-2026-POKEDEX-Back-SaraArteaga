package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.CommunityStats;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonUsageStat;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonViewPort;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private TeamPersistencePort teamPort;
    @Mock
    private PokemonViewPort pokemonViewPort;
    @Mock
    private PokemonService pokemonService;
    @InjectMocks
    private StatsServiceImpl service;

    @Test
    void communityStats_ranksMostChosenByCount() {
        Map<Long, Long> choices = new LinkedHashMap<>();
        choices.put(25L, 5L);
        choices.put(1L, 9L);
        when(teamPort.countChoicesByPokemon()).thenReturn(choices);
        when(pokemonViewPort.viewCountFor(25L)).thenReturn(2L);
        when(pokemonViewPort.viewCountFor(1L)).thenReturn(3L);
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).nationalNumber(25).name("Pikachu").build());
        when(pokemonService.findById(1L)).thenReturn(Pokemon.builder().id(1L).nationalNumber(1).name("Bulbasaur").build());
        when(pokemonViewPort.topViewed(10)).thenReturn(List.of(new PokemonUsageStat(1L, 1, "Bulbasaur", 3L, 0L)));
        when(pokemonViewPort.countTotalViews()).thenReturn(10L);
        when(teamPort.countAll()).thenReturn(4L);

        CommunityStats stats = service.communityStats();

        assertThat(stats.mostChosenInTeams()).hasSize(2);
        assertThat(stats.mostChosenInTeams().get(0).name()).isEqualTo("Bulbasaur");
        assertThat(stats.totalTeamsCreated()).isEqualTo(4L);
    }
}
