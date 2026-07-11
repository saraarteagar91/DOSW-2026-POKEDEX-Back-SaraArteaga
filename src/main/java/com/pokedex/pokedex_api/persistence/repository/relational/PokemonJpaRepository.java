package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.PokemonEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * {@code @EntityGraph} evita el problema N+1 al traer tipos/habilidades/movimientos/región/stats
 * en una sola consulta cuando se necesita el detalle completo (doc §11.2).
 */
public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long>,
        JpaSpecificationExecutor<PokemonEntity> {

    boolean existsByNationalNumber(Integer nationalNumber);

    Optional<PokemonEntity> findByNationalNumber(Integer nationalNumber);

    @EntityGraph(attributePaths = {"types", "abilities", "moves", "region", "stats"})
    Optional<PokemonEntity> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"types", "region"})
    List<PokemonEntity> findAllByOrderByNationalNumberAsc();

    @EntityGraph(attributePaths = {"types", "region"})
    @org.springframework.data.jpa.repository.Query("SELECT p FROM PokemonEntity p")
    org.springframework.data.domain.Page<PokemonEntity> findAllWithTypes(org.springframework.data.domain.Pageable pageable);
}
