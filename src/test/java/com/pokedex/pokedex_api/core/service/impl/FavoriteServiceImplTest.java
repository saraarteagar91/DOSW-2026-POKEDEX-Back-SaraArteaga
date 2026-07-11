package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.Favorite;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.FavoritePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoritePersistencePort favoritePort;
    @Mock
    private PokemonService pokemonService;
    @Mock
    private BadgeService badgeService;
    @InjectMocks
    private FavoriteServiceImpl service;

    @Test
    void toggleFavorite_whenNotFavorited_addsAndEvaluatesBadges() {
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).build());
        when(favoritePort.findByUserIdAndPokemonId(1L, 25L)).thenReturn(Optional.empty());

        boolean result = service.toggleFavorite(1L, 25L);

        assertThat(result).isTrue();
        verify(favoritePort).save(any());
        verify(badgeService).evaluate(1L);
    }

    @Test
    void toggleFavorite_whenAlreadyFavorited_removes() {
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).build());
        when(favoritePort.findByUserIdAndPokemonId(1L, 25L))
                .thenReturn(Optional.of(Favorite.builder().id(5L).userId(1L).pokemonId(25L).build()));

        boolean result = service.toggleFavorite(1L, 25L);

        assertThat(result).isFalse();
        verify(favoritePort).deleteByUserIdAndPokemonId(1L, 25L);
        verify(favoritePort, never()).save(any());
    }

    @Test
    void listFavorites_resolvesEachPokemon() {
        when(favoritePort.findByUserId(1L)).thenReturn(java.util.List.of(
                Favorite.builder().id(1L).userId(1L).pokemonId(25L).build()));
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).name("Pikachu").build());

        var favorites = service.listFavorites(1L);

        assertThat(favorites).hasSize(1);
        verify(pokemonService, times(1)).findById(25L);
    }
}
