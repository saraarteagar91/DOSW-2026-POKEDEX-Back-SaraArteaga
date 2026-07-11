package com.pokedex.pokedex_api.controller.mapper;

import com.pokedex.pokedex_api.controller.dto.response.TeamResponse;
import com.pokedex.pokedex_api.controller.dto.response.TeamSynergyResponse;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Mapper manual (sin MapStruct): la relación entre campos es directa, no justifica generar código adicional. */
@Component
@RequiredArgsConstructor
public class TeamDtoMapper {

    private final PokemonDtoMapper pokemonDtoMapper;

    public TeamResponse toResponse(Team team) {
        return new TeamResponse(team.getId(), team.getUserId(), team.getName(), team.getPokemonIds());
    }

    public TeamSynergyResponse toResponse(TeamSynergy synergy) {
        return new TeamSynergyResponse(
                synergy.typeCoverage(),
                synergy.weaknesses(),
                synergy.resistances(),
                synergy.immunities(),
                pokemonDtoMapper.toResponse(synergy.averageStats()));
    }
}
