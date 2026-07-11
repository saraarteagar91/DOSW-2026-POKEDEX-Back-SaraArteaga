package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.UserGameStats;
import java.util.Optional;

public interface UserGameStatsPersistencePort {
    Optional<UserGameStats> findByUserId(Long userId);

    UserGameStats save(UserGameStats stats);
}
