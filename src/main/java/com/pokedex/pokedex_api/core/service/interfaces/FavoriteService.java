package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Pokemon;
import java.util.List;

public interface FavoriteService {
    List<Pokemon> listFavorites(Long userId);

    /** @return true si quedó marcado como favorito, false si se quitó. */
    boolean toggleFavorite(Long userId, Long pokemonId);
}
