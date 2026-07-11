package com.pokedex.pokedex_api.core.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.BadgeCriteria;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.model.UserGameStats;
import com.pokedex.pokedex_api.core.service.interfaces.BadgePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.FavoritePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.UserGameStatsPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BadgeServiceImplTest {

    @Mock
    private BadgePersistencePort badgePort;
    @Mock
    private FavoritePersistencePort favoritePort;
    @Mock
    private TeamPersistencePort teamPort;
    @Mock
    private UserPersistencePort userPersistencePort;
    @Mock
    private UserGameStatsPersistencePort gameStatsPort;
    @InjectMocks
    private BadgeServiceImpl service;

    @Test
    void evaluate_unlocksFavoritesBadge_whenThresholdReached() {
        Badge badge = Badge.builder().id(1L).code("collector").name("Coleccionista")
                .criteriaType(BadgeCriteria.FAVORITES_COUNT).threshold(5).build();
        when(badgePort.findAllBadges()).thenReturn(List.of(badge));
        when(badgePort.isUnlocked(1L, "collector")).thenReturn(false);
        when(favoritePort.countByUserId(1L)).thenReturn(5L);

        service.evaluate(1L);

        verify(badgePort).unlock(1L, badge);
    }

    @Test
    void evaluate_skipsAlreadyUnlockedBadges() {
        Badge badge = Badge.builder().id(1L).code("collector").criteriaType(BadgeCriteria.FAVORITES_COUNT)
                .threshold(5).build();
        when(badgePort.findAllBadges()).thenReturn(List.of(badge));
        when(badgePort.isUnlocked(1L, "collector")).thenReturn(true);

        service.evaluate(1L);

        verify(badgePort, never()).unlock(eq(1L), any());
        verify(favoritePort, never()).countByUserId(any());
    }

    @Test
    void evaluate_fullTeamBadge_checksAnyTeamWithSixMembers() {
        Badge badge = Badge.builder().id(2L).code("full_squad").criteriaType(BadgeCriteria.FULL_TEAM)
                .threshold(6).build();
        when(badgePort.findAllBadges()).thenReturn(List.of(badge));
        when(badgePort.isUnlocked(1L, "full_squad")).thenReturn(false);
        when(teamPort.findByUserId(1L)).thenReturn(List.of(
                Team.builder().id(1L).userId(1L).pokemonIds(List.of(1L, 2L, 3L)).build(),
                Team.builder().id(2L).userId(1L).pokemonIds(List.of(1L, 2L, 3L, 4L, 5L, 6L)).build()));

        service.evaluate(1L);

        verify(badgePort, times(1)).unlock(1L, badge);
    }

    @Test
    void evaluate_minigameStreakBadge_usesBestStreak() {
        Badge badge = Badge.builder().id(3L).code("sharp_eye").criteriaType(BadgeCriteria.MINIGAME_STREAK)
                .threshold(5).build();
        when(badgePort.findAllBadges()).thenReturn(List.of(badge));
        when(badgePort.isUnlocked(1L, "sharp_eye")).thenReturn(false);
        when(gameStatsPort.findByUserId(1L)).thenReturn(Optional.of(
                UserGameStats.builder().userId(1L).currentStreak(2).bestStreak(6).build()));

        service.evaluate(1L);

        verify(badgePort).unlock(1L, badge);
    }

    @Test
    void evaluate_pokemonViewsBadge_readsUserCounter() {
        Badge badge = Badge.builder().id(4L).code("first_glance").criteriaType(BadgeCriteria.POKEMON_VIEWS)
                .threshold(1).build();
        when(badgePort.findAllBadges()).thenReturn(List.of(badge));
        when(badgePort.isUnlocked(1L, "first_glance")).thenReturn(false);
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(
                User.builder().id(1L).viewedPokemonCount(3).build()));

        service.evaluate(1L);

        verify(badgePort).unlock(1L, badge);
    }
}
