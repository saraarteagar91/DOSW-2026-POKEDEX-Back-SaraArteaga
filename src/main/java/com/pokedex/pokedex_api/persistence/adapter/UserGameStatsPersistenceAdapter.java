package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.UserGameStats;
import com.pokedex.pokedex_api.core.service.interfaces.UserGameStatsPersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.UserGameStatsEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.UserGameStatsJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGameStatsPersistenceAdapter implements UserGameStatsPersistencePort {

    private final UserGameStatsJpaRepository repository;

    @Override
    public Optional<UserGameStats> findByUserId(Long userId) {
        return repository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public UserGameStats save(UserGameStats stats) {
        UserGameStatsEntity entity = repository.findByUserId(stats.getUserId())
                .orElseGet(() -> UserGameStatsEntity.builder()
                        .userId(stats.getUserId())
                        .currentStreak(0)
                        .bestStreak(0)
                        .build());
        entity.setCurrentStreak(stats.getCurrentStreak());
        entity.setBestStreak(stats.getBestStreak());
        entity.setCurrentRoundPokemonId(stats.getCurrentRoundPokemonId());
        return toDomain(repository.save(entity));
    }

    private UserGameStats toDomain(UserGameStatsEntity entity) {
        return UserGameStats.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .currentStreak(entity.getCurrentStreak())
                .bestStreak(entity.getBestStreak())
                .currentRoundPokemonId(entity.getCurrentRoundPokemonId())
                .build();
    }
}
