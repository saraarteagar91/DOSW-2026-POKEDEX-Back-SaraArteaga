package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.persistence.entity.relational.TeamEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.TeamJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamPersistenceAdapterTest {

    @Mock
    private TeamJpaRepository repository;
    private TeamPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TeamPersistenceAdapter(repository);
    }

    @Test
    void save_whenNew_createsEntity() {
        Team team = Team.builder().userId(1L).name("Equipo").pokemonIds(List.of(1L, 2L)).build();
        when(repository.save(any())).thenAnswer(inv -> {
            TeamEntity e = inv.getArgument(0);
            return TeamEntity.builder().id(10L).userId(e.getUserId()).name(e.getName())
                    .pokemonIds(e.getPokemonIds()).build();
        });

        Team result = adapter.save(team);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getPokemonIds()).containsExactly(1L, 2L);
    }

    @Test
    void save_whenUpdating_mutatesExistingEntity() {
        TeamEntity existing = TeamEntity.builder().id(5L).userId(1L).name("Viejo")
                .pokemonIds(new java.util.ArrayList<>(List.of(1L))).build();
        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Team updated = adapter.save(Team.builder().id(5L).userId(1L).name("Nuevo").pokemonIds(List.of(3L, 4L)).build());

        assertThat(updated.getName()).isEqualTo("Nuevo");
        assertThat(updated.getPokemonIds()).containsExactly(3L, 4L);
    }

    @Test
    void countChoicesByPokemon_mapsNativeQueryRows() {
        TeamJpaRepository.PokemonChoiceCount row = new TeamJpaRepository.PokemonChoiceCount() {
            public Long getPokemonId() {
                return 25L;
            }

            public Long getTotal() {
                return 3L;
            }
        };
        when(repository.countChoicesByPokemon()).thenReturn(List.of(row));

        var result = adapter.countChoicesByPokemon();

        assertThat(result).containsEntry(25L, 3L);
    }

    @Test
    void countAll_delegates() {
        when(repository.count()).thenReturn(7L);
        assertThat(adapter.countAll()).isEqualTo(7L);
    }
}
