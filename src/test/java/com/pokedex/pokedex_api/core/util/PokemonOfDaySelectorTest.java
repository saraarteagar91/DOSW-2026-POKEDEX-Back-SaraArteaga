package com.pokedex.pokedex_api.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PokemonOfDaySelectorTest {

    @Test
    void indexForDate_isStableForSameDate() {
        LocalDate date = LocalDate.of(2026, 7, 10);
        int first = PokemonOfDaySelector.indexForDate(date, 34);
        int second = PokemonOfDaySelector.indexForDate(date, 34);
        assertThat(first).isEqualTo(second);
    }

    @Test
    void indexForDate_isWithinCatalogBounds() {
        int index = PokemonOfDaySelector.indexForDate(LocalDate.of(2026, 1, 1), 10);
        assertThat(index).isBetween(0, 9);
    }

    @Test
    void indexForDate_whenCatalogEmpty_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> PokemonOfDaySelector.indexForDate(LocalDate.now(), 0));
    }
}
