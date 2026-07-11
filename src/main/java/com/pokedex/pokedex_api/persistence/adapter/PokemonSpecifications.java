package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import com.pokedex.pokedex_api.persistence.entity.relational.AbilityEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.MoveEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.RegionEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.TypeEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * Combina los criterios de RF-02 (búsqueda) y RF-04 (filtros) en una única {@link Specification},
 * de forma que el listado se resuelva en una sola consulta (evita construir N filtros con N idas a BD).
 */
public final class PokemonSpecifications {

    private PokemonSpecifications() {
    }

    public static Specification<PokemonEntity> fromCriteria(PokemonFilterCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Long.class != query.getResultType()) {
                query.distinct(true);
            }

            if (criteria.search() != null && !criteria.search().isBlank()) {
                String like = "%" + criteria.search().toLowerCase() + "%";
                Predicate byName = cb.like(cb.lower(root.get("name")), like);
                Predicate byNumber = cb.disjunction();
                if (criteria.search().matches("\\d+")) {
                    try {
                        byNumber = cb.equal(root.get("nationalNumber"), Integer.parseInt(criteria.search()));
                    } catch (NumberFormatException tooLongToBeAValidNumber) {
                        // término numérico fuera de rango de Integer (p. ej. muchos dígitos): se ignora
                        // la búsqueda por número y se conserva solo la búsqueda por nombre.
                    }
                }
                predicates.add(cb.or(byName, byNumber));
            }
            if (criteria.region() != null && !criteria.region().isBlank()) {
                Join<PokemonEntity, RegionEntity> region = root.join("region");
                predicates.add(cb.equal(cb.lower(region.get("name")), criteria.region().toLowerCase()));
            }
            if (criteria.type() != null && !criteria.type().isBlank()) {
                Join<PokemonEntity, TypeEntity> type = root.join("types");
                predicates.add(cb.equal(cb.lower(type.get("name")), criteria.type().toLowerCase()));
            }
            if (criteria.ability() != null && !criteria.ability().isBlank()) {
                Join<PokemonEntity, AbilityEntity> ability = root.join("abilities");
                predicates.add(cb.equal(cb.lower(ability.get("name")), criteria.ability().toLowerCase()));
            }
            if (criteria.move() != null && !criteria.move().isBlank()) {
                Join<PokemonEntity, MoveEntity> move = root.join("moves");
                predicates.add(cb.equal(cb.lower(move.get("name")), criteria.move().toLowerCase()));
            }
            if (criteria.generation() != null) {
                predicates.add(cb.equal(root.get("generation"), criteria.generation()));
            }
            if (criteria.hasMega() != null) {
                predicates.add(cb.equal(root.get("hasMega"), criteria.hasMega()));
            }
            if (criteria.color() != null && !criteria.color().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("color")), criteria.color().toLowerCase()));
            }
            if (criteria.evolutionStage() != null) {
                predicates.add(cb.equal(root.get("evolutionStage"), criteria.evolutionStage()));
            }
            if (criteria.minTotalStats() != null || criteria.maxTotalStats() != null) {
                Join<Object, Object> stats = root.join("stats");
                var total = cb.sum(cb.sum(cb.sum(stats.get("hp"), stats.get("attack")),
                        cb.sum(stats.get("defense"), stats.get("specialAttack"))),
                        cb.sum(stats.get("specialDefense"), stats.get("speed")));
                if (criteria.minTotalStats() != null) {
                    predicates.add(cb.ge(total, criteria.minTotalStats()));
                }
                if (criteria.maxTotalStats() != null) {
                    predicates.add(cb.le(total, criteria.maxTotalStats()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
