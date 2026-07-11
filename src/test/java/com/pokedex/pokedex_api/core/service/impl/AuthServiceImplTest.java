package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.InvalidCredentialsException;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserPersistencePort userPersistencePort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthServiceImpl service;

    private User localUser;

    @BeforeEach
    void setUp() {
        localUser = User.builder()
                .id(1L).username("sara").email("sara@pokebloom.com").passwordHash("hashed")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true).build();
    }

    @Test
    void register_whenEmailTaken_throws() {
        when(userPersistencePort.existsByEmail("sara@pokebloom.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class,
                () -> service.register("sara", "sara@pokebloom.com", "clave1234", null));
    }

    @Test
    void register_whenUsernameTaken_throws() {
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByUsername("sara")).thenReturn(true);
        assertThrows(DuplicateResourceException.class,
                () -> service.register("sara", "sara2@pokebloom.com", "clave1234", null));
    }

    @Test
    void register_whenValid_encodesPasswordAndSavesTrainer() {
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode("clave1234")).thenReturn("hashed");
        when(userPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.register("sara", "sara@pokebloom.com", "clave1234", null);

        assertThat(result.getRole()).isEqualTo(Role.TRAINER);
        assertThat(result.getProvider()).isEqualTo(AuthProvider.LOCAL);
        assertThat(result.getPasswordHash()).isEqualTo("hashed");
    }

    @Test
    void authenticate_whenUserNotFound_throwsInvalidCredentials() {
        when(userPersistencePort.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class,
                () -> service.authenticate("nope@pokebloom.com", "x"));
    }

    @Test
    void authenticate_whenPasswordWrong_throwsInvalidCredentials() {
        when(userPersistencePort.findByEmail("sara@pokebloom.com")).thenReturn(Optional.of(localUser));
        when(passwordEncoder.matches("bad", "hashed")).thenReturn(false);
        assertThrows(InvalidCredentialsException.class,
                () -> service.authenticate("sara@pokebloom.com", "bad"));
    }

    @Test
    void authenticate_whenGoogleAccount_rejectsPasswordLogin() {
        User googleUser = localUser.toBuilder().provider(AuthProvider.GOOGLE).passwordHash(null).build();
        when(userPersistencePort.findByEmail("sara@pokebloom.com")).thenReturn(Optional.of(googleUser));
        assertThrows(InvalidCredentialsException.class,
                () -> service.authenticate("sara@pokebloom.com", "whatever"));
    }

    @Test
    void authenticate_whenValid_returnsUser() {
        when(userPersistencePort.findByEmail("sara@pokebloom.com")).thenReturn(Optional.of(localUser));
        when(passwordEncoder.matches("clave1234", "hashed")).thenReturn(true);
        User result = service.authenticate("sara@pokebloom.com", "clave1234");
        assertThat(result.getUsername()).isEqualTo("sara");
    }

    @Test
    void findOrCreateGoogleUser_whenExists_returnsExisting() {
        when(userPersistencePort.findByEmail("sara@pokebloom.com")).thenReturn(Optional.of(localUser));
        User result = service.findOrCreateGoogleUser("sara@pokebloom.com", "Sara", null);
        assertThat(result).isEqualTo(localUser);
    }

    @Test
    void findOrCreateGoogleUser_whenNew_createsTrainerWithoutPassword() {
        when(userPersistencePort.findByEmail("nueva@pokebloom.com")).thenReturn(Optional.empty());
        when(userPersistencePort.existsByUsername(anyString())).thenReturn(false);
        when(userPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.findOrCreateGoogleUser("nueva@pokebloom.com", "Nueva Entrenadora", "pic.png");

        assertThat(result.getProvider()).isEqualTo(AuthProvider.GOOGLE);
        assertThat(result.getPasswordHash()).isNull();
    }
}
