package com.pokedex.pokedex_api.core.model;

import java.util.List;

/** Resultado público de resolver un enlace compartido (RF-17). Solo uno de los dos campos aplica según el tipo. */
public record ShareResolution(
        ShareType type,
        Pokemon pokemon,
        String teamName,
        List<Pokemon> teamPokemon,
        TeamSynergy teamSynergy
) {
}
