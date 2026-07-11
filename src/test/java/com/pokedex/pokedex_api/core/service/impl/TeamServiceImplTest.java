package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import com.pokedex.pokedex_api.core.validator.TeamValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamPersistencePort teamPort;
    @Mock
    private PokemonService pokemonService;
    @Mock
    private BadgeService badgeService;
    private final TeamValidator teamValidator = new TeamValidator();
    private TeamServiceImpl service;

    private Team team;

    @BeforeEach
    void setUp() {
        service = new TeamServiceImpl(teamPort, pokemonService, teamValidator, badgeService);
        team = Team.builder().id(1L).userId(10L).name("Iniciales").pokemonIds(List.of(1L, 4L, 7L)).build();
    }

    @Test
    void create_whenNameDuplicateForUser_throws() {
        when(teamPort.existsByUserIdAndName(10L, "Iniciales")).thenReturn(true);
        assertThrows(DuplicateResourceException.class,
                () -> service.create(10L, "Iniciales", List.of(1L)));
    }

    @Test
    void create_whenMoreThanSixPokemon_throwsForbidden() {
        List<Long> seven = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertThrows(ForbiddenOperationException.class, () -> service.create(10L, "Grande", seven));
    }

    @Test
    void create_whenValid_savesAndEvaluatesBadges() {
        when(teamPort.existsByUserIdAndName(10L, "Iniciales")).thenReturn(false);
        when(pokemonService.findById(any())).thenReturn(somePokemon());
        when(teamPort.save(any())).thenReturn(team);

        Team result = service.create(10L, "Iniciales", List.of(1L, 4L, 7L));

        assertThat(result.getName()).isEqualTo("Iniciales");
    }

    @Test
    void update_whenNotOwner_throwsForbidden() {
        when(teamPort.findById(1L)).thenReturn(Optional.of(team));
        assertThrows(ForbiddenOperationException.class,
                () -> service.update(1L, 999L, "Otro", List.of(1L)));
    }

    @Test
    void update_whenTeamMissing_throwsNotFound() {
        when(teamPort.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, 10L, "Otro", List.of(1L)));
    }

    @Test
    void delete_whenOwner_deletes() {
        when(teamPort.findById(1L)).thenReturn(Optional.of(team));
        service.delete(1L, 10L);
    }

    @Test
    void synergyOf_computesUsingTeamMembers() {
        when(teamPort.findById(1L)).thenReturn(Optional.of(team));
        when(pokemonService.findById(any())).thenReturn(somePokemon());

        TeamSynergy synergy = service.synergyOf(1L, 10L);

        assertThat(synergy).isNotNull();
        assertThat(synergy.averageStats()).isNotNull();
    }

    private Pokemon somePokemon() {
        return Pokemon.builder()
                .id(1L).nationalNumber(1).name("Bulbasaur")
                .types(List.of("Grass", "Poison")).region("Kanto").generation(1).hasMega(false)
                .stats(PokemonStats.builder().hp(45).attack(49).defense(49)
                        .specialAttack(65).specialDefense(65).speed(45).build())
                .build();
    }
}
