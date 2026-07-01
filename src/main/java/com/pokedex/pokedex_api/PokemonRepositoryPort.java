package com.pokedex.pokedex_api;
import java.util.List;
import java.util.Optional;

public interface PokemonRepositoryPort {
    Optional<Pokemon> obtenerPorId(Long id);
    List<Pokemon> obtenerTodos();
    boolean existePorPokedexNumber(Integer pokedexNumber);
    Pokemon guardar(Pokemon pokemon);
    void eliminarPorId(Long id);
}