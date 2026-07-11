package com.pokedex.pokedex_api.controller.dto.response;

import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        AuthProvider provider,
        String avatarUrl,
        Boolean enabled,
        LocalDateTime createdAt
) {
}
