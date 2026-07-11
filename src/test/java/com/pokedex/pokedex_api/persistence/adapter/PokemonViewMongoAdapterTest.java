package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.persistence.entity.document.PokemonViewDocument;
import com.pokedex.pokedex_api.persistence.repository.document.PokemonViewMongoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PokemonViewMongoAdapterTest {

    @Mock
    private PokemonViewMongoRepository repository;
    private PokemonViewMongoAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PokemonViewMongoAdapter(repository);
    }

    @Test
    void registerView_whenNoDocument_createsWithCountOne() {
        when(repository.findByPokemonId(25L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        adapter.registerView(25L, 25, "Pikachu");

        verify(repository).save(argThatViewCountIs(1L));
    }

    @Test
    void registerView_whenDocumentExists_incrementsCount() {
        when(repository.findByPokemonId(25L)).thenReturn(Optional.of(
                PokemonViewDocument.builder().id("x").pokemonId(25L).pokemonName("Pikachu").viewCount(4L).build()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        adapter.registerView(25L, 25, "Pikachu");

        verify(repository).save(argThatViewCountIs(5L));
    }

    @Test
    void countTotalViews_sumsAllDocuments() {
        when(repository.findAll()).thenReturn(List.of(
                PokemonViewDocument.builder().viewCount(3L).build(),
                PokemonViewDocument.builder().viewCount(7L).build()));

        assertThat(adapter.countTotalViews()).isEqualTo(10L);
    }

    @Test
    void viewCountFor_whenMissing_returnsZero() {
        when(repository.findByPokemonId(99L)).thenReturn(Optional.empty());
        assertThat(adapter.viewCountFor(99L)).isZero();
    }

    private PokemonViewDocument argThatViewCountIs(long expected) {
        return org.mockito.ArgumentMatchers.argThat(doc -> doc.getViewCount() == expected);
    }
}
