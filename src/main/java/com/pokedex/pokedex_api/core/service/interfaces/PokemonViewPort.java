package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.PokemonUsageStat;
import java.util.List;

/**
 * Puerto hacia MongoDB (persistencia no relacional de vistas/consultas, doc §2 y §5.5).
 */
public interface PokemonViewPort {
    void registerView(Long pokemonId, Integer nationalNumber, String pokemonName);

    long countTotalViews();

    List<PokemonUsageStat> topViewed(int limit);

    long viewCountFor(Long pokemonId);
}
