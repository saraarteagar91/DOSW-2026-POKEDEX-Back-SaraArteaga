package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.FavoriteEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<FavoriteEntity> findByUserIdAndPokemonId(Long userId, Long pokemonId);

    long countByUserId(Long userId);

    boolean existsByPokemonId(Long pokemonId);

    void deleteByUserIdAndPokemonId(Long userId, Long pokemonId);
}
