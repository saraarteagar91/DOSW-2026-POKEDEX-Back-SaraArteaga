package com.pokedex.pokedex_api.controller.mapper;

import com.pokedex.pokedex_api.controller.dto.response.BadgeResponse;
import com.pokedex.pokedex_api.controller.dto.response.UserBadgeResponse;
import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.UserBadge;
import org.springframework.stereotype.Component;

@Component
public class BadgeDtoMapper {

    public BadgeResponse toResponse(Badge badge) {
        return new BadgeResponse(badge.getCode(), badge.getName(), badge.getDescription());
    }

    public UserBadgeResponse toResponse(UserBadge userBadge) {
        return new UserBadgeResponse(toResponse(userBadge.getBadge()), userBadge.getUnlockedAt());
    }
}
