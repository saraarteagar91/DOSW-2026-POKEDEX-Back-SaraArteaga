package com.pokedex.pokedex_api.controller.dto.response;

public record MiniGameAnswerResponse(
        boolean correct, PokemonResponse revealedPokemon, int currentStreak, int bestStreak
) {
}
