package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Team;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeamPersistencePort {
    Optional<Team> findById(Long id);

    List<Team> findByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    long countByUserId(Long userId);

    Team save(Team team);

    void deleteById(Long id);

    /** RF-10: veces que cada Pokémon aparece en algún equipo. */
    Map<Long, Long> countChoicesByPokemon();

    long countAll();
}
