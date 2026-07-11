package com.pokedex.pokedex_api.core.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserBadge {
    Long id;
    Long userId;
    Badge badge;
    LocalDateTime unlockedAt;
}
