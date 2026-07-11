package com.pokedex.pokedex_api.persistence.entity.document;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Documento no relacional de estadísticas de consulta (RF-03, RF-10). Cada ficha vista suma 1 al contador.
 */
@Document(collection = "pokemon_views")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonViewDocument {

    @Id
    private String id;

    @Field("pokemon_id")
    private Long pokemonId;

    @Field("pokemon_name")
    private String pokemonName;

    @Field("view_count")
    private Long viewCount;

    @Field("last_viewed")
    private LocalDateTime lastViewed;
}
