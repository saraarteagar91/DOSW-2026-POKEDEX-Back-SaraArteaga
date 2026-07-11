package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.UserGameStatsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameStatsJpaRepository extends JpaRepository<UserGameStatsEntity, Long> {
    Optional<UserGameStatsEntity> findByUserId(Long userId);
}
