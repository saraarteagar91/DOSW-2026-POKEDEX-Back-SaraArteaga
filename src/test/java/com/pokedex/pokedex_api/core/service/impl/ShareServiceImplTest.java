package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareResolution;
import com.pokedex.pokedex_api.core.model.ShareType;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.ShareLinkPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.TeamService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShareServiceImplTest {

    @Mock
    private ShareLinkPersistencePort shareLinkPort;
    @Mock
    private PokemonService pokemonService;
    @Mock
    private TeamService teamService;
    @InjectMocks
    private ShareServiceImpl service;

    @Test
    void createLink_forTeamNotOwned_throwsForbidden() {
        when(teamService.getById(1L)).thenReturn(Team.builder().id(1L).userId(999L).build());
        assertThrows(ForbiddenOperationException.class,
                () -> service.createLink(1L, ShareType.TEAM, 1L));
    }

    @Test
    void createLink_forOwnTeam_savesLink() {
        when(teamService.getById(1L)).thenReturn(Team.builder().id(1L).userId(1L).build());
        when(shareLinkPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ShareLink link = service.createLink(1L, ShareType.TEAM, 1L);

        assertThat(link.getToken()).isNotBlank();
    }

    @Test
    void resolve_whenTokenMissing_throwsNotFound() {
        when(shareLinkPort.findByToken("bad")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.resolve("bad"));
    }

    @Test
    void resolve_forPokemonLink_returnsPokemonResolution() {
        ShareLink link = ShareLink.builder().id(1L).token("abc").type(ShareType.POKEMON).refId(25L).ownerUserId(1L).build();
        when(shareLinkPort.findByToken("abc")).thenReturn(Optional.of(link));
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).name("Pikachu").build());

        ShareResolution resolution = service.resolve("abc");

        assertThat(resolution.type()).isEqualTo(ShareType.POKEMON);
        assertThat(resolution.pokemon().getName()).isEqualTo("Pikachu");
    }

    @Test
    void resolve_forTeamLink_returnsTeamResolutionWithSynergy() {
        ShareLink link = ShareLink.builder().id(2L).token("xyz").type(ShareType.TEAM).refId(1L).ownerUserId(1L).build();
        when(shareLinkPort.findByToken("xyz")).thenReturn(Optional.of(link));
        when(teamService.getById(1L)).thenReturn(
                Team.builder().id(1L).userId(1L).name("Equipo").pokemonIds(List.of(25L)).build());
        when(pokemonService.findById(25L)).thenReturn(Pokemon.builder().id(25L).name("Pikachu")
                .types(List.of("Electric"))
                .stats(com.pokedex.pokedex_api.core.model.PokemonStats.builder()
                        .hp(35).attack(55).defense(40).specialAttack(50).specialDefense(50).speed(90).build())
                .build());

        ShareResolution resolution = service.resolve("xyz");

        assertThat(resolution.type()).isEqualTo(ShareType.TEAM);
        assertThat(resolution.teamName()).isEqualTo("Equipo");
        assertThat(resolution.teamSynergy()).isNotNull();
    }
}
