package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.BusinessException;
import com.pokedex.pokedex_api.core.model.MiniGameAnswerResult;
import com.pokedex.pokedex_api.core.model.MiniGameRound;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.UserGameStats;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.MiniGameService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.UserGameStatsPersistencePort;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-16: minijuego ¿Quién es ese Pokémon? La racha se guarda en UserGameStats por usuario. */
@Service
@RequiredArgsConstructor
public class MiniGameServiceImpl implements MiniGameService {

    private final PokemonPersistencePort pokemonPort;
    private final PokemonService pokemonService;
    private final UserGameStatsPersistencePort gameStatsPort;
    private final BadgeService badgeService;

    @Override
    public MiniGameRound startRound(Long userId) {
        List<Pokemon> catalog = pokemonPort.findAllOrderedByNumber();
        if (catalog.isEmpty()) {
            throw new BusinessException("El catálogo está vacío, no hay ronda posible", "EMPTY_CATALOG");
        }
        Pokemon chosen = catalog.get(ThreadLocalRandom.current().nextInt(catalog.size()));
        UserGameStats stats = currentStats(userId).toBuilder().currentRoundPokemonId(chosen.getId()).build();
        gameStatsPort.save(stats);
        return new MiniGameRound(chosen.getImageUrl());
    }

    @Override
    public MiniGameAnswerResult answer(Long userId, String guess) {
        UserGameStats stats = currentStats(userId);
        if (stats.getCurrentRoundPokemonId() == null) {
            throw new BusinessException("No hay una ronda activa, inicia una nueva", "NO_ACTIVE_ROUND");
        }
        Pokemon expected = pokemonService.findById(stats.getCurrentRoundPokemonId());
        boolean correct = expected.getName().equalsIgnoreCase(guess == null ? "" : guess.trim());

        int newStreak = correct ? stats.getCurrentStreak() + 1 : 0;
        int bestStreak = Math.max(stats.getBestStreak(), newStreak);
        UserGameStats updated = stats.toBuilder()
                .currentStreak(newStreak)
                .bestStreak(bestStreak)
                .currentRoundPokemonId(null)
                .build();
        gameStatsPort.save(updated);

        if (correct) {
            badgeService.evaluate(userId);
        }
        return new MiniGameAnswerResult(correct, expected, newStreak, bestStreak);
    }

    private UserGameStats currentStats(Long userId) {
        return gameStatsPort.findByUserId(userId)
                .orElse(UserGameStats.builder().userId(userId).currentStreak(0).bestStreak(0).build());
    }
}
