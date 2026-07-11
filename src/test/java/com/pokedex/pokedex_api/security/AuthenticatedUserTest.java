package com.pokedex.pokedex_api.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import org.junit.jupiter.api.Test;

class AuthenticatedUserTest {

    @Test
    void exposesRoleAsSpringAuthority() {
        User user = User.builder().id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.ADMIN).provider(AuthProvider.LOCAL).enabled(true).build();
        AuthenticatedUser principal = new AuthenticatedUser(user);

        assertThat(principal.getUserId()).isEqualTo(1L);
        assertThat(principal.getUsername()).isEqualTo("sara@pokebloom.com");
        assertThat(principal.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_ADMIN");
        assertThat(principal.isEnabled()).isTrue();
        assertThat(principal.isAccountNonExpired()).isTrue();
        assertThat(principal.isAccountNonLocked()).isTrue();
        assertThat(principal.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void disabledUser_isReflectedInUserDetails() {
        User user = User.builder().id(2L).username("baneada").email("baneada@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(false).build();
        AuthenticatedUser principal = new AuthenticatedUser(user);

        assertThat(principal.isEnabled()).isFalse();
    }
}
