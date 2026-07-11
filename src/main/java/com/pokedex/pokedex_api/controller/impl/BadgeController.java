package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.BadgeApi;
import com.pokedex.pokedex_api.controller.dto.response.BadgeResponse;
import com.pokedex.pokedex_api.controller.dto.response.UserBadgeResponse;
import com.pokedex.pokedex_api.controller.mapper.BadgeDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BadgeController implements BadgeApi {

    private final BadgeService badgeService;
    private final BadgeDtoMapper mapper;

    @Override
    public ResponseEntity<List<BadgeResponse>> catalog() {
        return ResponseEntity.ok(badgeService.allBadges().stream().map(mapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<UserBadgeResponse>> mine(AuthenticatedUser principal) {
        return ResponseEntity.ok(badgeService.myBadges(principal.getUserId()).stream().map(mapper::toResponse).toList());
    }
}
