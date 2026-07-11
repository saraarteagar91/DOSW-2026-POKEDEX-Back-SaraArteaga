package com.pokedex.pokedex_api.core.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Favorite {
    Long id;
    Long userId;
    Long pokemonId;
    LocalDateTime createdAt;
}
