package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.BadgeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeJpaRepository extends JpaRepository<BadgeEntity, Long> {
    List<BadgeEntity> findAllByOrderByIdAsc();
}
