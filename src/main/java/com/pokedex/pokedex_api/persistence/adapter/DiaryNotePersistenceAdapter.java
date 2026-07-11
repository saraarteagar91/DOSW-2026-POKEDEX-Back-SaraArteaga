package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.DiaryNote;
import com.pokedex.pokedex_api.core.service.interfaces.DiaryNotePersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.DiaryNoteEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.DiaryNoteJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryNotePersistenceAdapter implements DiaryNotePersistencePort {

    private final DiaryNoteJpaRepository repository;

    @Override
    public Optional<DiaryNote> findByUserIdAndPokemonId(Long userId, Long pokemonId) {
        return repository.findByUserIdAndPokemonId(userId, pokemonId).map(this::toDomain);
    }

    @Override
    public DiaryNote save(DiaryNote note) {
        DiaryNoteEntity entity = repository.findByUserIdAndPokemonId(note.getUserId(), note.getPokemonId())
                .orElseGet(() -> DiaryNoteEntity.builder()
                        .userId(note.getUserId())
                        .pokemonId(note.getPokemonId())
                        .build());
        entity.setText(note.getText());
        return toDomain(repository.save(entity));
    }

    private DiaryNote toDomain(DiaryNoteEntity entity) {
        return DiaryNote.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .pokemonId(entity.getPokemonId())
                .text(entity.getText())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
