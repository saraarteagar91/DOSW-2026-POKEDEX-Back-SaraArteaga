package com.pokedex.pokedex_api.core.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * Nota personal privada de la entrenadora sobre un Pokémon (RF-18, RN-12).
 */
@Value
@Builder(toBuilder = true)
public class DiaryNote {
    Long id;
    Long userId;
    Long pokemonId;
    String text;
    LocalDateTime updatedAt;
}
