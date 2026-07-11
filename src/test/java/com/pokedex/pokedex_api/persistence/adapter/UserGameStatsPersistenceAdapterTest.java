package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.UserGameStats;
import com.pokedex.pokedex_api.persistence.entity.relational.UserGameStatsEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.UserGameStatsJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserGameStatsPersistenceAdapterTest {

    @Mock
    private UserGameStatsJpaRepository repository;
    private UserGameStatsPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserGameStatsPersistenceAdapter(repository);
    }

    @Test
    void save_whenNoneExists_createsNew() {
        when(repository.findByUserId(1L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserGameStats result = adapter.save(UserGameStats.builder()
                .userId(1L).currentStreak(1).bestStreak(1).currentRoundPokemonId(25L).build());

        assertThat(result.getCurrentStreak()).isEqualTo(1);
    }

    @Test
    void save_whenExists_updatesFields() {
        UserGameStatsEntity existing = UserGameStatsEntity.builder().id(1L).userId(1L)
                .currentStreak(2).bestStreak(4).build();
        when(repository.findByUserId(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserGameStats result = adapter.save(UserGameStats.builder()
                .userId(1L).currentStreak(5).bestStreak(5).currentRoundPokemonId(null).build());

        assertThat(result.getCurrentStreak()).isEqualTo(5);
        assertThat(result.getBestStreak()).isEqualTo(5);
    }
}
