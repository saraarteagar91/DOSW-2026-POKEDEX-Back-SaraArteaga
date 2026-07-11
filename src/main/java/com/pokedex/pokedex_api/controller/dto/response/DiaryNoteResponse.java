package com.pokedex.pokedex_api.controller.dto.response;

import java.time.LocalDateTime;

public record DiaryNoteResponse(Long pokemonId, String text, LocalDateTime updatedAt) {
}
