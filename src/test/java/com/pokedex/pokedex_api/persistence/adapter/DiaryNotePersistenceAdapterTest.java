package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.DiaryNote;
import com.pokedex.pokedex_api.persistence.entity.relational.DiaryNoteEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.DiaryNoteJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryNotePersistenceAdapterTest {

    @Mock
    private DiaryNoteJpaRepository repository;
    private DiaryNotePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new DiaryNotePersistenceAdapter(repository);
    }

    @Test
    void save_whenNoExistingNote_createsOne() {
        when(repository.findByUserIdAndPokemonId(1L, 25L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> {
            DiaryNoteEntity e = inv.getArgument(0);
            return DiaryNoteEntity.builder().id(1L).userId(e.getUserId()).pokemonId(e.getPokemonId())
                    .text(e.getText()).build();
        });

        DiaryNote result = adapter.save(DiaryNote.builder().userId(1L).pokemonId(25L).text("Nota").build());

        assertThat(result.getText()).isEqualTo("Nota");
    }

    @Test
    void save_whenExistingNote_updatesText() {
        DiaryNoteEntity existing = DiaryNoteEntity.builder().id(1L).userId(1L).pokemonId(25L).text("Vieja").build();
        when(repository.findByUserIdAndPokemonId(1L, 25L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DiaryNote result = adapter.save(DiaryNote.builder().userId(1L).pokemonId(25L).text("Nueva").build());

        assertThat(result.getText()).isEqualTo("Nueva");
    }
}
