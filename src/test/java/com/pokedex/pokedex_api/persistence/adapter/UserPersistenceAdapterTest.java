package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex_api.persistence.mapper.UserPersistenceMapperImpl;
import com.pokedex.pokedex_api.persistence.repository.relational.UserJpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock
    private UserJpaRepository repository;
    private UserPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserPersistenceAdapter(repository, new UserPersistenceMapperImpl());
    }

    private UserEntity entity() {
        return UserEntity.builder().id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true)
                .viewedPokemonCount(0).createdAt(LocalDateTime.now()).build();
    }

    @Test
    void findByEmail_delegatesCaseInsensitive() {
        when(repository.findByEmailIgnoreCase("sara@pokebloom.com")).thenReturn(Optional.of(entity()));
        assertThat(adapter.findByEmail("sara@pokebloom.com")).isPresent();
    }

    @Test
    void save_whenNewUser_persists() {
        User user = User.builder().username("sara").email("sara@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true).build();
        when(repository.save(any())).thenReturn(entity());

        User result = adapter.save(user);

        assertThat(result.getUsername()).isEqualTo("sara");
    }

    @Test
    void save_whenUpdatingExisting_preservesCreatedAt() {
        User user = User.builder().id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.ADMIN).provider(AuthProvider.LOCAL).enabled(true).build();
        UserEntity existing = entity();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = adapter.save(user);

        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
        assertThat(result.getCreatedAt()).isEqualTo(existing.getCreatedAt());
    }

    @Test
    void incrementViewedCount_delegates() {
        adapter.incrementViewedCount(1L);
        org.mockito.Mockito.verify(repository).incrementViewedCount(1L);
    }

    @Test
    void countByRole_delegates() {
        when(repository.countByRole(Role.ADMIN)).thenReturn(2L);
        assertThat(adapter.countByRole(Role.ADMIN)).isEqualTo(2L);
    }
}
