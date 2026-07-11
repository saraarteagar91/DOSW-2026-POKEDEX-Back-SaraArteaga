package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.User;

public interface AuthService {
    User register(String username, String email, String rawPassword, String avatarUrl);

    User authenticate(String email, String rawPassword);

    User findOrCreateGoogleUser(String email, String name, String avatarUrl);
}
