package com.pokedex.pokedex_api.persistence.mapper;

import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.persistence.entity.relational.AbilityEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.MoveEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonStatsEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.TypeEntity;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct: Entidad de negocio (core) &lt;-&gt; DAO relacional (doc §7.4 / §11.2).
 * Las relaciones (tipos, región, habilidades, movimientos, stats) se resuelven en el adapter porque
 * requieren consultar/crear otras entidades — aquí solo se listan como ignoradas al mapear a entidad.
 */
@Mapper(componentModel = "spring")
public interface PokemonPersistenceMapper {

    @Mapping(target = "region", expression = "java(entity.getRegion() != null ? entity.getRegion().getName() : null)")
    @Mapping(target = "types", expression = "java(mapNames(entity.getTypes()))")
    @Mapping(target = "abilities", expression = "java(mapAbilityNames(entity.getAbilities()))")
    @Mapping(target = "moves", expression = "java(mapMoveNames(entity.getMoves()))")
    @Mapping(target = "stats", source = "stats")
    Pokemon toDomain(PokemonEntity entity);

    PokemonStats toStats(PokemonStatsEntity statsEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "types", ignore = true)
    @Mapping(target = "abilities", ignore = true)
    @Mapping(target = "moves", ignore = true)
    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PokemonEntity toEntity(Pokemon pokemon);

    default List<String> mapNames(Collection<TypeEntity> types) {
        return types == null ? List.of() : types.stream().map(TypeEntity::getName).toList();
    }

    default List<String> mapAbilityNames(Collection<AbilityEntity> abilities) {
        return abilities == null ? List.of() : abilities.stream().map(AbilityEntity::getName).toList();
    }

    default List<String> mapMoveNames(Collection<MoveEntity> moves) {
        return moves == null ? List.of() : moves.stream().map(MoveEntity::getName).toList();
    }
}
