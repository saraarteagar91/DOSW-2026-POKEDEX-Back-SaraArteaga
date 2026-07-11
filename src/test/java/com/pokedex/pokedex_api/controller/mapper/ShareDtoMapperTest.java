package com.pokedex.pokedex_api.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareResolution;
import com.pokedex.pokedex_api.core.model.ShareType;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ShareDtoMapperTest {

    private ShareDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShareDtoMapper(new PokemonDtoMapperImpl(), new TeamDtoMapper(new PokemonDtoMapperImpl()));
        ReflectionTestUtils.setField(mapper, "shareBaseUrl", "http://localhost:8080/api/v1/share");
    }

    @Test
    void toResponse_link_buildsFullShareUrl() {
        ShareLink link = ShareLink.builder().id(1L).token("abc123").type(ShareType.POKEMON).refId(1L).ownerUserId(1L).build();
        var response = mapper.toResponse(link);
        assertThat(response.shareUrl()).isEqualTo("http://localhost:8080/api/v1/share/abc123");
    }

    @Test
    void toResponse_pokemonResolution_omitsTeamFields() {
        Pokemon pikachu = Pokemon.builder().id(25L).name("Pikachu").build();
        ShareResolution resolution = new ShareResolution(ShareType.POKEMON, pikachu, null, null, null);

        var response = mapper.toResponse(resolution);

        assertThat(response.pokemon().name()).isEqualTo("Pikachu");
        assertThat(response.teamPokemon()).isNull();
    }

    @Test
    void toResponse_teamResolution_includesSynergyAndOmitsPokemon() {
        Pokemon pikachu = Pokemon.builder().id(25L).name("Pikachu")
                .stats(PokemonStats.builder().hp(1).attack(1).defense(1)
                        .specialAttack(1).specialDefense(1).speed(1).build())
                .build();
        TeamSynergy synergy = new TeamSynergy(Map.of(), List.of(), List.of(), List.of(),
                PokemonStats.builder().hp(1).attack(1).defense(1).specialAttack(1).specialDefense(1).speed(1).build());
        ShareResolution resolution = new ShareResolution(ShareType.TEAM, null, "Equipo", List.of(pikachu), synergy);

        var response = mapper.toResponse(resolution);

        assertThat(response.pokemon()).isNull();
        assertThat(response.teamName()).isEqualTo("Equipo");
        assertThat(response.teamPokemon()).hasSize(1);
        assertThat(response.teamSynergy()).isNotNull();
    }
}
