package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.DiaryNote;
import java.util.Optional;

public interface DiaryService {
    Optional<DiaryNote> getNote(Long userId, Long pokemonId);

    DiaryNote upsertNote(Long userId, Long pokemonId, String text);
}
