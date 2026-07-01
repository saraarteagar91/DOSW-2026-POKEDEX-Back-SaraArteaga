package com.pokedex.pokedex_api;
import java.util.List;

public interface PokemonService {
    List<Pokemon> listar();
    Pokemon obtenerPorId(Long id);
    Pokemon registrar(Pokemon pokemon);
    void eliminar(Long id);
}