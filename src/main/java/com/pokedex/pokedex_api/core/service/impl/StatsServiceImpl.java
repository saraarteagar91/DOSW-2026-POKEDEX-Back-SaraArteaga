package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.model.CommunityStats;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonUsageStat;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonViewPort;
import com.pokedex.pokedex_api.core.service.interfaces.StatsService;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-10, RN-08: tasa de elección (relacional, team_pokemon) + vistas (MongoDB) + ranking. */
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private static final int TOP_LIMIT = 10;

    private final TeamPersistencePort teamPort;
    private final PokemonViewPort pokemonViewPort;
    private final PokemonService pokemonService;

    @Override
    public CommunityStats communityStats() {
        List<PokemonUsageStat> mostChosen = mostChosenInTeams();
        List<PokemonUsageStat> mostViewed = pokemonViewPort.topViewed(TOP_LIMIT).stream()
                .map(stat -> enrich(stat.pokemonId(), stat.viewCount(), 0L))
                .toList();

        return new CommunityStats(mostViewed, mostChosen, pokemonViewPort.countTotalViews(), teamPort.countAll());
    }

    private List<PokemonUsageStat> mostChosenInTeams() {
        Map<Long, Long> choices = teamPort.countChoicesByPokemon();
        return choices.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(TOP_LIMIT)
                .map(entry -> enrich(entry.getKey(), pokemonViewPort.viewCountFor(entry.getKey()), entry.getValue()))
                .sorted(Comparator.comparingLong(PokemonUsageStat::choiceCount).reversed())
                .toList();
    }

    private PokemonUsageStat enrich(Long pokemonId, long viewCount, long choiceCount) {
        try {
            Pokemon pokemon = pokemonService.findById(pokemonId);
            return new PokemonUsageStat(pokemonId, pokemon.getNationalNumber(), pokemon.getName(),
                    viewCount, choiceCount);
        } catch (RuntimeException notFound) {
            return new PokemonUsageStat(pokemonId, null, "Desconocido", viewCount, choiceCount);
        }
    }
}
