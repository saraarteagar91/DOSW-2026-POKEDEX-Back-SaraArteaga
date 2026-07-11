package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonViewPort;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PokemonServiceImplTest {

    @Mock
    private PokemonPersistencePort pokemonPort;
    @Mock
    private PokemonViewPort pokemonViewPort;
    @Mock
    private UserPersistencePort userPersistencePort;
    @Mock
    private BadgeService badgeService;

    private PokemonServiceImpl service;
    private Pokemon pikachu;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-07-10T00:00:00Z"), ZoneOffset.UTC);
        service = new PokemonServiceImpl(pokemonPort, pokemonViewPort, userPersistencePort, badgeService, fixedClock);
        pikachu = Pokemon.builder()
                .id(25L).nationalNumber(25).name("Pikachu")
                .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                .stats(PokemonStats.builder().hp(35).attack(55).defense(40)
                        .specialAttack(50).specialDefense(50).speed(90).build())
                .build();
    }

    @Test
    void findById_whenExists_returnsPokemon() {
        when(pokemonPort.findById(25L)).thenReturn(Optional.of(pikachu));
        Pokemon result = service.findById(25L);
        assertThat(result.getName()).isEqualTo("Pikachu");
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void create_whenDuplicateNumber_throws() {
        when(pokemonPort.existsByNationalNumber(25)).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.create(pikachu));
        verify(pokemonPort, never()).save(any());
    }

    @Test
    void create_whenValid_saves() {
        when(pokemonPort.existsByNationalNumber(25)).thenReturn(false);
        when(pokemonPort.save(any())).thenReturn(pikachu);
        Pokemon result = service.create(pikachu);
        assertThat(result.getName()).isEqualTo("Pikachu");
    }

    @Test
    void update_keepsOriginalNationalNumber_evenIfRequestChangesIt() {
        when(pokemonPort.findById(25L)).thenReturn(Optional.of(pikachu));
        Pokemon incoming = pikachu.toBuilder().nationalNumber(999).name("Raichu").build();
        when(pokemonPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Pokemon updated = service.update(25L, incoming);

        assertThat(updated.getNationalNumber()).isEqualTo(25);
        assertThat(updated.getName()).isEqualTo("Raichu");
    }

    @Test
    void delete_whenReferenced_throwsForbidden() {
        when(pokemonPort.findById(25L)).thenReturn(Optional.of(pikachu));
        when(pokemonPort.isReferencedByTeamOrFavorite(25L)).thenReturn(true);
        assertThrows(ForbiddenOperationException.class, () -> service.delete(25L));
        verify(pokemonPort, never()).deleteById(any());
    }

    @Test
    void delete_whenNotReferenced_deletes() {
        when(pokemonPort.findById(25L)).thenReturn(Optional.of(pikachu));
        when(pokemonPort.isReferencedByTeamOrFavorite(25L)).thenReturn(false);
        service.delete(25L);
        verify(pokemonPort, times(1)).deleteById(25L);
    }

    @Test
    void viewDetail_registersViewAndIncrementsUserCounter() {
        when(pokemonPort.findById(25L)).thenReturn(Optional.of(pikachu));
        service.viewDetail(25L, 7L);
        verify(pokemonViewPort).registerView(25L, 25, "Pikachu");
        verify(userPersistencePort).incrementViewedCount(7L);
        verify(badgeService).evaluate(7L);
    }

    @Test
    void viewDetail_whenAnonymous_doesNotTouchUserCounters() {
        when(pokemonPort.findById(25L)).thenReturn(Optional.of(pikachu));
        service.viewDetail(25L, null);
        verify(userPersistencePort, never()).incrementViewedCount(any());
        verify(badgeService, never()).evaluate(any());
    }

    @Test
    void pokemonOfTheDay_isDeterministicForSameDate() {
        List<Pokemon> catalog = List.of(pikachu, pikachu.toBuilder().id(1L).nationalNumber(1).build());
        when(pokemonPort.findAllOrderedByNumber()).thenReturn(catalog);
        Pokemon first = service.pokemonOfTheDay();
        Pokemon second = service.pokemonOfTheDay();
        assertThat(first.getId()).isEqualTo(second.getId());
    }

    @Test
    void pokemonOfTheDay_whenCatalogEmpty_throws() {
        when(pokemonPort.findAllOrderedByNumber()).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> service.pokemonOfTheDay());
    }
}
