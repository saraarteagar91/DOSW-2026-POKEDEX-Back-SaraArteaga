package com.pokedex.pokedex_api.controller.dto.response;

import java.time.LocalDateTime;

public record UserBadgeResponse(BadgeResponse badge, LocalDateTime unlockedAt) {
}
