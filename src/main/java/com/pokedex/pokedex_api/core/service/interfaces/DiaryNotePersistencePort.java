package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.DiaryNote;
import java.util.Optional;

public interface DiaryNotePersistencePort {
    Optional<DiaryNote> findByUserIdAndPokemonId(Long userId, Long pokemonId);

    DiaryNote save(DiaryNote note);
}
