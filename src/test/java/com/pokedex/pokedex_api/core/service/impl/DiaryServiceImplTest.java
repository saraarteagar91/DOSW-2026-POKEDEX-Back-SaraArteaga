package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.BusinessException;
import com.pokedex.pokedex_api.core.model.DiaryNote;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.service.interfaces.DiaryNotePersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryServiceImplTest {

    @Mock
    private DiaryNotePersistencePort diaryPort;
    @Mock
    private PokemonService pokemonService;
    @InjectMocks
    private DiaryServiceImpl service;

    @Test
    void upsertNote_whenBlank_throws() {
        when(pokemonService.findById(1L)).thenReturn(Pokemon.builder().id(1L).build());
        assertThrows(BusinessException.class, () -> service.upsertNote(1L, 1L, "   "));
    }

    @Test
    void upsertNote_whenTooLong_throws() {
        when(pokemonService.findById(1L)).thenReturn(Pokemon.builder().id(1L).build());
        String tooLong = "x".repeat(2001);
        assertThrows(BusinessException.class, () -> service.upsertNote(1L, 1L, tooLong));
    }

    @Test
    void upsertNote_whenValid_saves() {
        when(pokemonService.findById(1L)).thenReturn(Pokemon.builder().id(1L).build());
        when(diaryPort.save(any())).thenReturn(
                DiaryNote.builder().id(1L).userId(1L).pokemonId(1L).text("Mi nota").build());

        DiaryNote result = service.upsertNote(1L, 1L, "Mi nota");

        assertThat(result.getText()).isEqualTo("Mi nota");
    }
}
