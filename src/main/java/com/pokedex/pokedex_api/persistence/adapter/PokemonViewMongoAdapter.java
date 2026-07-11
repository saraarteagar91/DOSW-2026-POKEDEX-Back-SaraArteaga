package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.PokemonUsageStat;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonViewPort;
import com.pokedex.pokedex_api.persistence.entity.document.PokemonViewDocument;
import com.pokedex.pokedex_api.persistence.repository.document.PokemonViewMongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Persistencia no relacional de vistas (RF-03, RF-10) — colección {@code pokemon_views} en MongoDB. */
@Component
@RequiredArgsConstructor
public class PokemonViewMongoAdapter implements PokemonViewPort {

    private final PokemonViewMongoRepository repository;

    @Override
    public void registerView(Long pokemonId, Integer nationalNumber, String pokemonName) {
        PokemonViewDocument document = repository.findByPokemonId(pokemonId)
                .orElseGet(() -> PokemonViewDocument.builder()
                        .pokemonId(pokemonId)
                        .pokemonName(pokemonName)
                        .viewCount(0L)
                        .build());
        document.setPokemonName(pokemonName);
        document.setViewCount(document.getViewCount() + 1);
        document.setLastViewed(LocalDateTime.now());
        repository.save(document);
    }

    @Override
    public long countTotalViews() {
        return repository.findAll().stream().mapToLong(PokemonViewDocument::getViewCount).sum();
    }

    @Override
    public List<PokemonUsageStat> topViewed(int limit) {
        return repository.findTop10ByOrderByViewCountDesc().stream()
                .limit(limit)
                .map(doc -> new PokemonUsageStat(doc.getPokemonId(), null, doc.getPokemonName(),
                        doc.getViewCount(), 0L))
                .toList();
    }

    @Override
    public long viewCountFor(Long pokemonId) {
        return repository.findByPokemonId(pokemonId).map(PokemonViewDocument::getViewCount).orElse(0L);
    }
}
