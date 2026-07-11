package com.pokedex.pokedex_api.persistence.repository.document;

import com.pokedex.pokedex_api.persistence.entity.document.PokemonViewDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PokemonViewMongoRepository extends MongoRepository<PokemonViewDocument, String> {
    Optional<PokemonViewDocument> findByPokemonId(Long pokemonId);

    List<PokemonViewDocument> findTop10ByOrderByViewCountDesc();
}
