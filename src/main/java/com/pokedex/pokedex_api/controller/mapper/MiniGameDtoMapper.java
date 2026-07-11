package com.pokedex.pokedex_api.controller.mapper;

import com.pokedex.pokedex_api.controller.dto.response.MiniGameAnswerResponse;
import com.pokedex.pokedex_api.controller.dto.response.MiniGameRoundResponse;
import com.pokedex.pokedex_api.core.model.MiniGameAnswerResult;
import com.pokedex.pokedex_api.core.model.MiniGameRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MiniGameDtoMapper {

    private final PokemonDtoMapper pokemonDtoMapper;

    public MiniGameRoundResponse toResponse(MiniGameRound round) {
        return new MiniGameRoundResponse(round.imageUrl());
    }

    public MiniGameAnswerResponse toResponse(MiniGameAnswerResult result) {
        return new MiniGameAnswerResponse(
                result.correct(),
                pokemonDtoMapper.toResponse(result.revealedPokemon()),
                result.currentStreak(),
                result.bestStreak());
    }
}
