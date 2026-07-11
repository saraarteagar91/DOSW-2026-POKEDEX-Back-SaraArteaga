package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.model.Favorite;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.FavoritePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.FavoriteService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-07, RN-12: favoritos privados por entrenadora. */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoritePersistencePort favoritePort;
    private final PokemonService pokemonService;
    private final BadgeService badgeService;

    @Override
    public List<Pokemon> listFavorites(Long userId) {
        return favoritePort.findByUserId(userId).stream()
                .map(favorite -> pokemonService.findById(favorite.getPokemonId()))
                .toList();
    }

    @Override
    public boolean toggleFavorite(Long userId, Long pokemonId) {
        pokemonService.findById(pokemonId);
        var existing = favoritePort.findByUserIdAndPokemonId(userId, pokemonId);
        if (existing.isPresent()) {
            favoritePort.deleteByUserIdAndPokemonId(userId, pokemonId);
            return false;
        }
        favoritePort.save(Favorite.builder().userId(userId).pokemonId(pokemonId).build());
        badgeService.evaluate(userId);
        return true;
    }
}
