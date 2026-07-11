package com.pokedex.pokedex_api.controller.dto.response;

public record FavoriteToggleResponse(Long pokemonId, boolean favorited) {
}
