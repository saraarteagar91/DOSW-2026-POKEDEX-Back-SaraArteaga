package com.pokedex.pokedex_api.core.model;

import lombok.Builder;
import lombok.Value;

/**
 * Estado del minijuego ¿Quién es ese Pokémon? por usuario (RF-16): racha actual, mejor racha
 * y el Pokémon esperado de la ronda en curso (para validar la siguiente respuesta).
 */
@Value
@Builder(toBuilder = true)
public class UserGameStats {
    Long id;
    Long userId;
    Integer currentStreak;
    Integer bestStreak;
    Long currentRoundPokemonId;
}
