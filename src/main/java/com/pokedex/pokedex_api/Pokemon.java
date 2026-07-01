package com.pokedex.pokedex_api;
import lombok.Value;
import lombok.Builder;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Pokemon {
    Long id;
    Integer pokedexNumber;
    String name;
    String imageUrl;
    List<String> types;
    String region;
    Integer generation;
    Boolean hasMega;
    PokemonStats stats;
}