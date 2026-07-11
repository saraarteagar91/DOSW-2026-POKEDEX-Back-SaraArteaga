package com.pokedex.pokedex_api.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "unaClaveDePruebaSuficientementeLargaParaHS256Testing");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);
    }

    private AuthenticatedUser sara() {
        return new AuthenticatedUser(User.builder()
                .id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true).build());
    }

    @Test
    void generateToken_thenExtractEmail_roundTrips() {
        String token = jwtService.generateToken(sara());
        assertThat(jwtService.extractEmail(token)).isEqualTo("sara@pokebloom.com");
    }

    @Test
    void generateToken_thenExtractUserId_roundTrips() {
        String token = jwtService.generateToken(sara());
        assertThat(jwtService.extractUserId(token)).isEqualTo(1L);
    }

    @Test
    void isTokenValid_forFreshToken_isTrue() {
        String token = jwtService.generateToken(sara());
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_forExpiredToken_isFalse() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L);
        String token = jwtService.generateToken(sara());
        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void isTokenValid_forGarbageToken_isFalse() {
        assertThat(jwtService.isTokenValid("no-es-un-jwt")).isFalse();
    }
}
