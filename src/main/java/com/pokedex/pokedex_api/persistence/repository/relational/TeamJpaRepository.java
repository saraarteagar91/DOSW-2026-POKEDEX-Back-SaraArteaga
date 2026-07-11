package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.TeamEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    List<TeamEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    long countByUserId(Long userId);

    /**
     * Tasa de elección (RF-10): cuántas veces aparece cada Pokémon en algún equipo, vía tabla
     * relacional {@code team_pokemon} generada por el {@code @ElementCollection} de TeamEntity.
     */
    @Query(value = "SELECT pokemon_id AS pokemonId, COUNT(*) AS total FROM team_pokemon GROUP BY pokemon_id",
            nativeQuery = true)
    List<PokemonChoiceCount> countChoicesByPokemon();

    @Query(value = "SELECT EXISTS(SELECT 1 FROM team_pokemon WHERE pokemon_id = :pokemonId)", nativeQuery = true)
    boolean existsPokemonInAnyTeam(Long pokemonId);

    interface PokemonChoiceCount {
        Long getPokemonId();

        Long getTotal();
    }
}
