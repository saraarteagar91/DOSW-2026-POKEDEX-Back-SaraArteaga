package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.Favorite;
import com.pokedex.pokedex_api.core.service.interfaces.FavoritePersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.FavoriteEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.FavoriteJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoritePersistenceAdapter implements FavoritePersistencePort {

    private final FavoriteJpaRepository repository;

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Favorite> findByUserIdAndPokemonId(Long userId, Long pokemonId) {
        return repository.findByUserIdAndPokemonId(userId, pokemonId).map(this::toDomain);
    }

    @Override
    public long countByUserId(Long userId) {
        return repository.countByUserId(userId);
    }

    @Override
    public Favorite save(Favorite favorite) {
        FavoriteEntity entity = FavoriteEntity.builder()
                .userId(favorite.getUserId())
                .pokemonId(favorite.getPokemonId())
                .build();
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteByUserIdAndPokemonId(Long userId, Long pokemonId) {
        repository.deleteByUserIdAndPokemonId(userId, pokemonId);
    }

    private Favorite toDomain(FavoriteEntity entity) {
        return Favorite.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .pokemonId(entity.getPokemonId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
