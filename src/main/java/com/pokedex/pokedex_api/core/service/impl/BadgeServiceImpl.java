package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.model.UserBadge;
import com.pokedex.pokedex_api.core.model.UserGameStats;
import com.pokedex.pokedex_api.core.service.interfaces.BadgePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.FavoritePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.UserGameStatsPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * RF-15 (Modo Coleccionista), RN-11 (solo se otorgan automáticamente). Se ejecuta en un hilo virtual
 * ({@code @Async}) para no bloquear la respuesta HTTP que disparó el hito (RNF-04, rendimiento).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeServiceImpl implements BadgeService {

    private final BadgePersistencePort badgePort;
    private final FavoritePersistencePort favoritePort;
    private final TeamPersistencePort teamPort;
    private final UserPersistencePort userPersistencePort;
    private final UserGameStatsPersistencePort gameStatsPort;

    @Override
    public List<Badge> allBadges() {
        return badgePort.findAllBadges();
    }

    @Override
    public List<UserBadge> myBadges(Long userId) {
        return badgePort.findUnlockedByUser(userId);
    }

    @Override
    @Async
    public void evaluate(Long userId) {
        for (Badge badge : badgePort.findAllBadges()) {
            if (badgePort.isUnlocked(userId, badge.getCode())) {
                continue;
            }
            if (meetsCriteria(userId, badge)) {
                log.info("PokéBloom: insignia '{}' desbloqueada para el usuario {}", badge.getName(), userId);
                badgePort.unlock(userId, badge);
            }
        }
    }

    private boolean meetsCriteria(Long userId, Badge badge) {
        int threshold = badge.getThreshold();
        return switch (badge.getCriteriaType()) {
            case POKEMON_VIEWS -> userPersistencePort.findById(userId)
                    .map(User::getViewedPokemonCount).orElse(0) >= threshold;
            case FAVORITES_COUNT -> favoritePort.countByUserId(userId) >= threshold;
            case TEAMS_CREATED -> teamPort.countByUserId(userId) >= threshold;
            case FULL_TEAM -> teamPort.findByUserId(userId).stream()
                    .anyMatch(team -> sizeOf(team) >= threshold);
            case MINIGAME_STREAK -> gameStatsPort.findByUserId(userId)
                    .map(UserGameStats::getBestStreak).orElse(0) >= threshold;
        };
    }

    private int sizeOf(Team team) {
        return team.getPokemonIds() == null ? 0 : team.getPokemonIds().size();
    }
}
