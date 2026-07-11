package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.response.StatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Stats", description = "RF-10, RN-08: estadísticas de la comunidad")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/stats")
public interface StatsApi {

    @Operation(summary = "Panel de estadísticas de la comunidad", description = "Visible para TRAINER y ADMIN")
    @GetMapping
    ResponseEntity<StatsResponse> communityStats();
}
