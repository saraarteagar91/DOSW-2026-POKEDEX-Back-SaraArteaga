package com.pokedex.pokedex_api.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.MiniGameAnswerResult;
import com.pokedex.pokedex_api.core.model.MiniGameRound;
import com.pokedex.pokedex_api.core.model.Pokemon;
import org.junit.jupiter.api.Test;

class MiniGameDtoMapperTest {

    private final MiniGameDtoMapper mapper = new MiniGameDtoMapper(new PokemonDtoMapperImpl());

    @Test
    void toResponse_round_mapsImageUrl() {
        var response = mapper.toResponse(new MiniGameRound("pika.png"));
        assertThat(response.imageUrl()).isEqualTo("pika.png");
    }

    @Test
    void toResponse_answer_mapsRevealedPokemonAndStreaks() {
        Pokemon pikachu = Pokemon.builder().id(25L).name("Pikachu").build();
        var response = mapper.toResponse(new MiniGameAnswerResult(true, pikachu, 3, 5));

        assertThat(response.correct()).isTrue();
        assertThat(response.revealedPokemon().name()).isEqualTo("Pikachu");
        assertThat(response.currentStreak()).isEqualTo(3);
        assertThat(response.bestStreak()).isEqualTo(5);
    }
}
