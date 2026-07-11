package com.pokedex.pokedex_api.core.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Equipo de la entrenadora (RF-08, RF-09). Máximo 6 Pokémon, validado en TeamValidator (RN-04).
 */
@Value
@Builder(toBuilder = true)
public class Team {
    Long id;
    Long userId;
    String name;
    List<Long> pokemonIds;
}
