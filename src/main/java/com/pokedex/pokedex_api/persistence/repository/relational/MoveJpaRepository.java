package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.MoveEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveJpaRepository extends JpaRepository<MoveEntity, Long> {
    Optional<MoveEntity> findByNameIgnoreCase(String name);
}
