package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.StatsApi;
import com.pokedex.pokedex_api.controller.dto.response.StatsResponse;
import com.pokedex.pokedex_api.controller.mapper.StatsDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final StatsService statsService;
    private final StatsDtoMapper mapper;

    @Override
    public ResponseEntity<StatsResponse> communityStats() {
        return ResponseEntity.ok(mapper.toResponse(statsService.communityStats()));
    }
}
