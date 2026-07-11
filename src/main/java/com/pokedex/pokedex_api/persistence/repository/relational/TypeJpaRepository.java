package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.TypeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeJpaRepository extends JpaRepository<TypeEntity, Long> {
    Optional<TypeEntity> findByNameIgnoreCase(String name);

    List<TypeEntity> findAllByOrderByNameAsc();
}
