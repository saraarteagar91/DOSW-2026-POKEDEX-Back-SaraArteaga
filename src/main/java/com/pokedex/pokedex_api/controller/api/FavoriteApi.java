package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.response.FavoriteToggleResponse;
import com.pokedex.pokedex_api.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Favorites", description = "RF-07, RN-12: favoritos privados de cada entrenadora")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/favorites")
public interface FavoriteApi {

    @Operation(summary = "Ver mis favoritos")
    @GetMapping
    ResponseEntity<List<PokemonResponse>> list(@AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Agregar o quitar de favoritos", description = "Alterna el estado (toggle)")
    @PostMapping("/{pokemonId}")
    ResponseEntity<FavoriteToggleResponse> toggle(@PathVariable Long pokemonId,
                                                   @AuthenticationPrincipal AuthenticatedUser principal);
}
