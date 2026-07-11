package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.BusinessException;
import com.pokedex.pokedex_api.core.model.DiaryNote;
import com.pokedex.pokedex_api.core.service.interfaces.DiaryNotePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.DiaryService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-18, RN-12: diario personal privado de notas por Pokémon. */
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private static final int MAX_LENGTH = 2000;

    private final DiaryNotePersistencePort diaryPort;
    private final PokemonService pokemonService;

    @Override
    public Optional<DiaryNote> getNote(Long userId, Long pokemonId) {
        return diaryPort.findByUserIdAndPokemonId(userId, pokemonId);
    }

    @Override
    public DiaryNote upsertNote(Long userId, Long pokemonId, String text) {
        pokemonService.findById(pokemonId);
        if (text == null || text.isBlank()) {
            throw new BusinessException("La nota no puede estar vacía", "EMPTY_NOTE");
        }
        if (text.length() > MAX_LENGTH) {
            throw new BusinessException("La nota supera el máximo de " + MAX_LENGTH + " caracteres", "NOTE_TOO_LONG");
        }
        return diaryPort.save(DiaryNote.builder().userId(userId).pokemonId(pokemonId).text(text).build());
    }
}
