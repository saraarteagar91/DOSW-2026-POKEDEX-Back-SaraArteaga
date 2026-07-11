package com.pokedex.pokedex_api.core.model;

public record MiniGameAnswerResult(boolean correct, Pokemon revealedPokemon, int currentStreak, int bestStreak) {
}
