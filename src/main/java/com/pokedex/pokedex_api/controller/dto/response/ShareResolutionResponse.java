package com.pokedex.pokedex_api.controller.dto.response;

import com.pokedex.pokedex_api.core.model.ShareType;
import java.util.List;

public record ShareResolutionResponse(
        ShareType type,
        PokemonResponse pokemon,
        String teamName,
        List<PokemonResponse> teamPokemon,
        TeamSynergyResponse teamSynergy
) {
}
