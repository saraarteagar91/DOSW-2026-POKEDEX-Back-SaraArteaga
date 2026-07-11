package com.pokedex.pokedex_api.core.util;

import java.time.LocalDate;

/**
 * Selección determinística del Pokémon del día (RF-19): misma fecha siempre produce el mismo índice,
 * sin necesitar una tabla nueva en base de datos.
 */
public final class PokemonOfDaySelector {

    private PokemonOfDaySelector() {
    }

    public static int indexForDate(LocalDate date, int catalogSize) {
        if (catalogSize <= 0) {
            throw new IllegalArgumentException("catalogSize debe ser mayor a 0");
        }
        long epochDay = date.toEpochDay();
        return (int) Math.floorMod(epochDay, catalogSize);
    }
}
