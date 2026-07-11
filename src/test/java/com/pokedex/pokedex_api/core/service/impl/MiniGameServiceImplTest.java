package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.BusinessException;
import com.pokedex.pokedex_api.core.model.MiniGameAnswerResult;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.UserGameStats;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.UserGameStatsPersistencePort;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MiniGameServiceImplTest {

    @Mock
    private PokemonPersistencePort pokemonPort;
    @Mock
    private PokemonService pokemonService;
    @Mock
    private UserGameStatsPersistencePort gameStatsPort;
    @Mock
    private BadgeService badgeService;
    @InjectMocks
    private MiniGameServiceImpl service;

    @Test
    void startRound_whenCatalogEmpty_throws() {
        when(pokemonPort.findAllOrderedByNumber()).thenReturn(List.of());
        assertThrows(BusinessException.class, () -> service.startRound(1L));
    }

    @Test
    void startRound_picksPokemonAndSavesExpectedAnswer() {
        Pokemon pikachu = Pokemon.builder().id(25L).name("Pikachu").imageUrl("pika.png").build();
        when(pokemonPort.findAllOrderedByNumber()).thenReturn(List.of(pikachu));
        when(gameStatsPort.findByUserId(1L)).thenReturn(Optional.empty());

        var round = service.startRound(1L);

        assertThat(round.imageUrl()).isEqualTo("pika.png");
    }

    @Test
    void answer_whenNoActiveRound_throws() {
        when(gameStatsPort.findByUserId(1L)).thenReturn(Optional.of(
                UserGameStats.builder().userId(1L).currentStreak(0).bestStreak(0).currentRoundPokemonId(null).build()));
        assertThrows(BusinessException.class, () -> service.answer(1L, "Pikachu"));
    }

    @Test
    void answer_whenCorrect_incrementsStreakAndEvaluatesBadges() {
        when(gameStatsPort.findByUserId(1L)).thenReturn(Optional.of(
                UserGameStats.builder().userId(1L).currentStreak(2).bestStreak(4).currentRoundPokemonId(25L).build()));
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).name("Pikachu").build());

        MiniGameAnswerResult result = service.answer(1L, "pikachu");

        assertThat(result.correct()).isTrue();
        assertThat(result.currentStreak()).isEqualTo(3);
        assertThat(result.bestStreak()).isEqualTo(4);
    }

    @Test
    void answer_whenIncorrect_resetsStreakToZero() {
        when(gameStatsPort.findByUserId(1L)).thenReturn(Optional.of(
                UserGameStats.builder().userId(1L).currentStreak(3).bestStreak(3).currentRoundPokemonId(25L).build()));
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).name("Pikachu").build());

        MiniGameAnswerResult result = service.answer(1L, "Charmander");

        assertThat(result.correct()).isFalse();
        assertThat(result.currentStreak()).isZero();
        assertThat(result.bestStreak()).isEqualTo(3);
    }
}
