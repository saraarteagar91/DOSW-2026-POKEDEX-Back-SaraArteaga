package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.Favorite;
import com.pokedex.pokedex_api.persistence.entity.relational.FavoriteEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.FavoriteJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoritePersistenceAdapterTest {

    @Mock
    private FavoriteJpaRepository repository;
    private FavoritePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FavoritePersistenceAdapter(repository);
    }

    @Test
    void findByUserId_mapsList() {
        when(repository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(
                FavoriteEntity.builder().id(1L).userId(1L).pokemonId(25L).createdAt(LocalDateTime.now()).build()));
        assertThat(adapter.findByUserId(1L)).hasSize(1);
    }

    @Test
    void save_persistsNewFavorite() {
        when(repository.save(any())).thenReturn(
                FavoriteEntity.builder().id(1L).userId(1L).pokemonId(25L).createdAt(LocalDateTime.now()).build());

        Favorite result = adapter.save(Favorite.builder().userId(1L).pokemonId(25L).build());

        assertThat(result.getPokemonId()).isEqualTo(25L);
    }

    @Test
    void deleteByUserIdAndPokemonId_delegates() {
        adapter.deleteByUserIdAndPokemonId(1L, 25L);
        verify(repository).deleteByUserIdAndPokemonId(1L, 25L);
    }

    @Test
    void findByUserIdAndPokemonId_whenMissing_returnsEmpty() {
        when(repository.findByUserIdAndPokemonId(1L, 99L)).thenReturn(Optional.empty());
        assertThat(adapter.findByUserIdAndPokemonId(1L, 99L)).isEmpty();
    }
}
