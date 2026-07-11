package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.BadgeCriteria;
import com.pokedex.pokedex_api.persistence.entity.relational.BadgeEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.UserBadgeEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.BadgeJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.UserBadgeJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BadgePersistenceAdapterTest {

    @Mock
    private BadgeJpaRepository badgeRepository;
    @Mock
    private UserBadgeJpaRepository userBadgeRepository;
    private BadgePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BadgePersistenceAdapter(badgeRepository, userBadgeRepository);
    }

    private BadgeEntity badgeEntity() {
        return BadgeEntity.builder().id(1L).code("collector").name("Coleccionista")
                .description("desc").criteriaType(BadgeCriteria.FAVORITES_COUNT).threshold(5).build();
    }

    @Test
    void findAllBadges_mapsList() {
        when(badgeRepository.findAllByOrderByIdAsc()).thenReturn(List.of(badgeEntity()));
        assertThat(adapter.findAllBadges()).hasSize(1);
    }

    @Test
    void findUnlockedByUser_mapsNestedBadge() {
        UserBadgeEntity ub = UserBadgeEntity.builder().id(1L).userId(1L).badge(badgeEntity())
                .unlockedAt(LocalDateTime.now()).build();
        when(userBadgeRepository.findByUserIdOrderByUnlockedAtDesc(1L)).thenReturn(List.of(ub));

        var result = adapter.findUnlockedByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBadge().getCode()).isEqualTo("collector");
    }

    @Test
    void unlock_whenAlreadyUnlocked_doesNothing() {
        when(userBadgeRepository.existsByUserIdAndBadge_Code(1L, "collector")).thenReturn(true);
        Badge badge = Badge.builder().id(1L).code("collector").criteriaType(BadgeCriteria.FAVORITES_COUNT).threshold(5).build();

        adapter.unlock(1L, badge);

        verify(userBadgeRepository, never()).save(any());
    }

    @Test
    void unlock_whenNotYetUnlocked_saves() {
        when(userBadgeRepository.existsByUserIdAndBadge_Code(1L, "collector")).thenReturn(false);
        Badge badge = Badge.builder().id(1L).code("collector").criteriaType(BadgeCriteria.FAVORITES_COUNT).threshold(5).build();

        adapter.unlock(1L, badge);

        verify(userBadgeRepository).save(any());
    }
}
