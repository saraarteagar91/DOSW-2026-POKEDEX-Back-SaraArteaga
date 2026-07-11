package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.PokemonStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonStatsJpaRepository extends JpaRepository<PokemonStatsEntity, Long> {
}
