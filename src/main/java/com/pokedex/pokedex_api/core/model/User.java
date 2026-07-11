package com.pokedex.pokedex_api.core.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * Entidad de negocio de usuario. POJO puro, sin anotaciones JPA (viven en persistence/entity/relational).
 */
@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String username;
    String email;
    String passwordHash;
    Role role;
    AuthProvider provider;
    String avatarUrl;
    Boolean enabled;
    Integer viewedPokemonCount;
    LocalDateTime createdAt;
}
