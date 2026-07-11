package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PokemonService {
    Page<Pokemon> findAll(Pageable pageable);

    Page<Pokemon> search(PokemonFilterCriteria criteria, Pageable pageable);

    Pokemon findById(Long id);

    /**
     * RF-03: además de traer la ficha, registra la visita en Mongo. Si {@code viewerUserId} no es nulo
     * (entrenadora autenticada) también suma a su contador personal y evalúa insignias (RF-15).
     */
    Pokemon viewDetail(Long id, Long viewerUserId);

    Pokemon create(Pokemon pokemon);

    Pokemon update(Long id, Pokemon pokemon);

    void delete(Long id);

    /** RF-19: selección determinística según la fecha. */
    Pokemon pokemonOfTheDay();
}
