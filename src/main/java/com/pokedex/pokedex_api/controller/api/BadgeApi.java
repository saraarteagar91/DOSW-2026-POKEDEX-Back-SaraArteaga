package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.response.BadgeResponse;
import com.pokedex.pokedex_api.controller.dto.response.UserBadgeResponse;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Badges", description = "RF-15, RN-11: Modo Coleccionista, insignias otorgadas automáticamente")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/badges")
public interface BadgeApi {

    @Operation(summary = "Catálogo completo de insignias")
    @GetMapping
    ResponseEntity<List<BadgeResponse>> catalog();

    @Operation(summary = "Álbum de logros de la entrenadora autenticada")
    @GetMapping("/me")
    ResponseEntity<List<UserBadgeResponse>> mine(@AuthenticationPrincipal AuthenticatedUser principal);
}
