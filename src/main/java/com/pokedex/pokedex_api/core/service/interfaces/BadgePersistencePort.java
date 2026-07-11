package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.UserBadge;
import java.util.List;

public interface BadgePersistencePort {
    List<Badge> findAllBadges();

    List<UserBadge> findUnlockedByUser(Long userId);

    boolean isUnlocked(Long userId, String badgeCode);

    void unlock(Long userId, Badge badge);
}
