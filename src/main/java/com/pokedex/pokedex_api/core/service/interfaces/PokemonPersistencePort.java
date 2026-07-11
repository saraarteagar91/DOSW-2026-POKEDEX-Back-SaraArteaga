package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Puerto de persistencia del catálogo (principio de inversión de dependencias, doc §6.4):
 * el core define lo que necesita, persistence lo implementa en un adapter.
 */
public interface PokemonPersistencePort {
    Optional<Pokemon> findById(Long id);

    Optional<Pokemon> findByNationalNumber(Integer nationalNumber);

    Page<Pokemon> findAll(Pageable pageable);

    Page<Pokemon> search(PokemonFilterCriteria criteria, Pageable pageable);

    List<Pokemon> findAllOrderedByNumber();

    boolean existsByNationalNumber(Integer nationalNumber);

    boolean isReferencedByTeamOrFavorite(Long pokemonId);

    Pokemon save(Pokemon pokemon);

    void deleteById(Long id);
}
