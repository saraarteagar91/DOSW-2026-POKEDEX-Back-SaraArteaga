package com.pokedex.pokedex_api.core.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Modelo de negocio del catálogo (RF-01..04, RF-11..13). Sin anotaciones JPA.
 */
@Value
@Builder(toBuilder = true)
public class Pokemon {
    Long id;
    Integer nationalNumber;
    String name;
    String description;
    String imageUrl;
    List<String> types;
    String region;
    Integer generation;
    Boolean hasMega;
    Integer evolutionStage;
    Long evolvesFromId;
    String color;
    List<String> abilities;
    List<String> moves;
    PokemonStats stats;
}
