package com.pokedex.pokedex_api.controller.mapper;

import com.pokedex.pokedex_api.controller.dto.response.PokemonUsageStatResponse;
import com.pokedex.pokedex_api.controller.dto.response.StatsResponse;
import com.pokedex.pokedex_api.core.model.CommunityStats;
import com.pokedex.pokedex_api.core.model.PokemonUsageStat;
import org.springframework.stereotype.Component;

@Component
public class StatsDtoMapper {

    public StatsResponse toResponse(CommunityStats stats) {
        return new StatsResponse(
                stats.mostViewed().stream().map(this::toResponse).toList(),
                stats.mostChosenInTeams().stream().map(this::toResponse).toList(),
                stats.totalViews(),
                stats.totalTeamsCreated());
    }

    private PokemonUsageStatResponse toResponse(PokemonUsageStat stat) {
        return new PokemonUsageStatResponse(
                stat.pokemonId(), stat.nationalNumber(), stat.name(), stat.viewCount(), stat.choiceCount());
    }
}
