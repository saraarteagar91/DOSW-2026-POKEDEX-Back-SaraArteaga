package com.pokedex.pokedex_api;
import org.springframework.stereotype.Component;

@Component
public class PokemonDtoMapper {
    public Pokemon aModelo(PokemonRequest request) {
        return Pokemon.builder()
                .pokedexNumber(request.pokedexNumber())
                .name(request.name())
                .imageUrl(request.imageUrl())
                .generation(request.generation())
                .hasMega(false)
                .build();
    }
    public PokemonResponse aRespuesta(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.getId(),
                pokemon.getPokedexNumber(),
                pokemon.getName(),
                pokemon.getImageUrl(),
                pokemon.getGeneration()
        );
    }
}