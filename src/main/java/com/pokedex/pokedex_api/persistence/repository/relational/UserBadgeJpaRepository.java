package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.UserBadgeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeJpaRepository extends JpaRepository<UserBadgeEntity, Long> {

    @EntityGraph(attributePaths = "badge")
    List<UserBadgeEntity> findByUserIdOrderByUnlockedAtDesc(Long userId);

    boolean existsByUserIdAndBadge_Code(Long userId, String badgeCode);
}
