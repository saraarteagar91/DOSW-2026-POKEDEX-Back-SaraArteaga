package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.FavoriteApi;
import com.pokedex.pokedex_api.controller.dto.response.FavoriteToggleResponse;
import com.pokedex.pokedex_api.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex_api.controller.mapper.PokemonDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.FavoriteService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteController implements FavoriteApi {

    private final FavoriteService favoriteService;
    private final PokemonDtoMapper mapper;

    @Override
    public ResponseEntity<List<PokemonResponse>> list(AuthenticatedUser principal) {
        return ResponseEntity.ok(mapper.toResponseList(favoriteService.listFavorites(principal.getUserId())));
    }

    @Override
    public ResponseEntity<FavoriteToggleResponse> toggle(Long pokemonId, AuthenticatedUser principal) {
        boolean favorited = favoriteService.toggleFavorite(principal.getUserId(), pokemonId);
        return ResponseEntity.ok(new FavoriteToggleResponse(pokemonId, favorited));
    }
}
