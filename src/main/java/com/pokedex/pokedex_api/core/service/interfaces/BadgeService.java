package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.UserBadge;
import java.util.List;

public interface BadgeService {
    List<Badge> allBadges();

    List<UserBadge> myBadges(Long userId);

    /** Evalúa los hitos del usuario y desbloquea automáticamente las insignias que correspondan (RN-11). */
    void evaluate(Long userId);
}
