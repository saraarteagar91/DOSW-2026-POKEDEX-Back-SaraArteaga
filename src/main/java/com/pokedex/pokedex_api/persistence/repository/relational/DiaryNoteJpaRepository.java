package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.DiaryNoteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryNoteJpaRepository extends JpaRepository<DiaryNoteEntity, Long> {
    Optional<DiaryNoteEntity> findByUserIdAndPokemonId(Long userId, Long pokemonId);
}
