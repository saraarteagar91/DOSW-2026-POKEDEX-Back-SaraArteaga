package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.InvalidCredentialsException;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.AuthService;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** RF-05 (registro), RF-06 (login con credenciales o Gmail). RN-01: correo único. RN-10: contraseña mínima 8. */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String username, String email, String rawPassword, String avatarUrl) {
        if (userPersistencePort.existsByEmail(email)) {
            throw new DuplicateResourceException("Usuario", "email", email);
        }
        if (userPersistencePort.existsByUsername(username)) {
            throw new DuplicateResourceException("Usuario", "username", username);
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(Role.TRAINER)
                .provider(AuthProvider.LOCAL)
                .avatarUrl(avatarUrl)
                .enabled(true)
                .build();
        log.info("PokéBloom: registrando nueva entrenadora {}", username);
        return userPersistencePort.save(user);
    }

    @Override
    public User authenticate(String email, String rawPassword) {
        User user = userPersistencePort.findByEmail(email).orElseThrow(InvalidCredentialsException::new);
        if (user.getProvider() != AuthProvider.LOCAL || user.getPasswordHash() == null) {
            throw new InvalidCredentialsException();
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new InvalidCredentialsException();
        }
        return user;
    }

    @Override
    public User findOrCreateGoogleUser(String email, String name, String avatarUrl) {
        return userPersistencePort.findByEmail(email).orElseGet(() -> {
            String baseUsername = (name == null || name.isBlank())
                    ? email.substring(0, email.indexOf('@'))
                    : name.replaceAll("\\s+", "").toLowerCase();
            String username = uniqueUsername(baseUsername);
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .passwordHash(null)
                    .role(Role.TRAINER)
                    .provider(AuthProvider.GOOGLE)
                    .avatarUrl(avatarUrl)
                    .enabled(true)
                    .build();
            log.info("PokéBloom: creando entrenadora vía Google {}", email);
            return userPersistencePort.save(user);
        });
    }

    private String uniqueUsername(String base) {
        String candidate = base.isBlank() ? "trainer" : base;
        int suffix = 0;
        while (userPersistencePort.existsByUsername(candidate)) {
            suffix++;
            candidate = base + suffix;
        }
        return candidate;
    }
}
