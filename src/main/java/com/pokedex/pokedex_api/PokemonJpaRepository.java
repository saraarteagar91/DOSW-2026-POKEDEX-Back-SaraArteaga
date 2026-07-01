package com.pokedex.pokedex_api;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long> {
    boolean existsByNationalNumber(Integer nationalNumber);
}