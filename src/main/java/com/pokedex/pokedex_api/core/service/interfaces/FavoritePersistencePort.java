package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Favorite;
import java.util.List;
import java.util.Optional;

public interface FavoritePersistencePort {
    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByUserIdAndPokemonId(Long userId, Long pokemonId);

    long countByUserId(Long userId);

    Favorite save(Favorite favorite);

    void deleteByUserIdAndPokemonId(Long userId, Long pokemonId);
}
