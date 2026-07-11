package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.UserBadge;
import com.pokedex.pokedex_api.core.service.interfaces.BadgePersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.BadgeEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.UserBadgeEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.BadgeJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.UserBadgeJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** RN-11: las insignias solo se otorgan automáticamente desde BadgeService, nunca se editan a mano. */
@Component
@RequiredArgsConstructor
public class BadgePersistenceAdapter implements BadgePersistencePort {

    private final BadgeJpaRepository badgeRepository;
    private final UserBadgeJpaRepository userBadgeRepository;

    @Override
    public List<Badge> findAllBadges() {
        return badgeRepository.findAllByOrderByIdAsc().stream().map(this::toDomain).toList();
    }

    @Override
    public List<UserBadge> findUnlockedByUser(Long userId) {
        return userBadgeRepository.findByUserIdOrderByUnlockedAtDesc(userId).stream()
                .map(entity -> UserBadge.builder()
                        .id(entity.getId())
                        .userId(entity.getUserId())
                        .badge(toDomain(entity.getBadge()))
                        .unlockedAt(entity.getUnlockedAt())
                        .build())
                .toList();
    }

    @Override
    public boolean isUnlocked(Long userId, String badgeCode) {
        return userBadgeRepository.existsByUserIdAndBadge_Code(userId, badgeCode);
    }

    @Override
    public void unlock(Long userId, Badge badge) {
        if (isUnlocked(userId, badge.getCode())) {
            return;
        }
        BadgeEntity badgeEntity = BadgeEntity.builder().id(badge.getId()).build();
        userBadgeRepository.save(UserBadgeEntity.builder().userId(userId).badge(badgeEntity).build());
    }

    private Badge toDomain(BadgeEntity entity) {
        return Badge.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .criteriaType(entity.getCriteriaType())
                .threshold(entity.getThreshold())
                .build();
    }
}
