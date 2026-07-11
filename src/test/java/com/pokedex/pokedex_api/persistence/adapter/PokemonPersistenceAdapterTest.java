package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonStatsEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.RegionEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.TypeEntity;
import com.pokedex.pokedex_api.persistence.mapper.PokemonPersistenceMapperImpl;
import com.pokedex.pokedex_api.persistence.repository.relational.AbilityJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.FavoriteJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.MoveJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.PokemonStatsJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.RegionJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.TeamJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.TypeJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PokemonPersistenceAdapterTest {

    @Mock
    private PokemonJpaRepository pokemonRepository;
    @Mock
    private PokemonStatsJpaRepository statsRepository;
    @Mock
    private TypeJpaRepository typeRepository;
    @Mock
    private RegionJpaRepository regionRepository;
    @Mock
    private AbilityJpaRepository abilityRepository;
    @Mock
    private MoveJpaRepository moveRepository;
    @Mock
    private FavoriteJpaRepository favoriteRepository;
    @Mock
    private TeamJpaRepository teamRepository;

    private PokemonPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PokemonPersistenceAdapter(pokemonRepository, statsRepository, typeRepository,
                regionRepository, abilityRepository, moveRepository, favoriteRepository, teamRepository,
                new PokemonPersistenceMapperImpl());
    }

    private PokemonEntity entity(Long id) {
        return PokemonEntity.builder().id(id).nationalNumber(25).name("Pikachu")
                .generation(1).hasMega(false).evolutionStage(1)
                .types(new java.util.LinkedHashSet<>()).abilities(new java.util.LinkedHashSet<>())
                .moves(new java.util.LinkedHashSet<>()).build();
    }

    @Test
    void findById_whenPresent_mapsToDomain() {
        when(pokemonRepository.findWithDetailsById(25L)).thenReturn(Optional.of(entity(25L)));
        Optional<Pokemon> result = adapter.findById(25L);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pikachu");
    }

    @Test
    void findById_whenMissing_returnsEmpty() {
        when(pokemonRepository.findWithDetailsById(99L)).thenReturn(Optional.empty());
        assertThat(adapter.findById(99L)).isEmpty();
    }

    @Test
    void findAll_delegatesToRepositoryPageQuery() {
        when(pokemonRepository.findAllWithTypes(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(entity(1L))));
        Page<Pokemon> page = adapter.findAll(Pageable.unpaged());
        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void search_usesSpecification() {
        when(pokemonRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(entity(1L))));
        Page<Pokemon> page = adapter.search(new PokemonFilterCriteria(null, null, "Electric", null, null, null,
                null, null, null, null, null), Pageable.unpaged());
        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void existsByNationalNumber_delegates() {
        when(pokemonRepository.existsByNationalNumber(25)).thenReturn(true);
        assertThat(adapter.existsByNationalNumber(25)).isTrue();
    }

    @Test
    void isReferencedByTeamOrFavorite_whenFavorited_returnsTrue() {
        when(favoriteRepository.existsByPokemonId(25L)).thenReturn(true);
        assertThat(adapter.isReferencedByTeamOrFavorite(25L)).isTrue();
    }

    @Test
    void isReferencedByTeamOrFavorite_whenNeither_returnsFalse() {
        when(favoriteRepository.existsByPokemonId(25L)).thenReturn(false);
        when(teamRepository.existsPokemonInAnyTeam(25L)).thenReturn(false);
        assertThat(adapter.isReferencedByTeamOrFavorite(25L)).isFalse();
    }

    @Test
    void save_whenCreating_resolvesCatalogAndPersists() {
        Pokemon pokemon = Pokemon.builder()
                .nationalNumber(25).name("Pikachu").types(List.of("Electric")).region("Kanto")
                .generation(1).hasMega(false).abilities(List.of("Static")).moves(List.of("Thunder Shock"))
                .stats(PokemonStats.builder().hp(35).attack(55).defense(40)
                        .specialAttack(50).specialDefense(50).speed(90).build())
                .build();

        when(regionRepository.findByNameIgnoreCase("Kanto"))
                .thenReturn(Optional.of(RegionEntity.builder().id(1L).name("Kanto").build()));
        when(typeRepository.findByNameIgnoreCase("Electric"))
                .thenReturn(Optional.of(TypeEntity.builder().id(1L).name("Electric").build()));
        when(abilityRepository.findByNameIgnoreCase(any())).thenReturn(Optional.empty());
        when(abilityRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(moveRepository.findByNameIgnoreCase(any())).thenReturn(Optional.empty());
        when(moveRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        PokemonEntity saved = entity(25L);
        when(pokemonRepository.save(any())).thenReturn(saved);
        when(pokemonRepository.findWithDetailsById(25L)).thenReturn(Optional.of(saved));

        Pokemon result = adapter.save(pokemon);

        assertThat(result.getId()).isEqualTo(25L);
        verify(statsRepository, times(1)).save(any(PokemonStatsEntity.class));
    }

    @Test
    void deleteById_delegatesToRepository() {
        adapter.deleteById(25L);
        verify(pokemonRepository).deleteById(25L);
    }

    @Test
    void findAllOrderedByNumber_mapsAllEntities() {
        when(pokemonRepository.findAllByOrderByNationalNumberAsc()).thenReturn(List.of(entity(1L), entity(2L)));
        assertThat(adapter.findAllOrderedByNumber()).hasSize(2);
    }
}
