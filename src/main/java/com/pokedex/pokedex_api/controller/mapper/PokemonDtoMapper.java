package com.pokedex.pokedex_api.controller.mapper;

import com.pokedex.pokedex_api.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex_api.controller.dto.request.PokemonStatsRequest;
import com.pokedex.pokedex_api.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex_api.controller.dto.response.PokemonStatsResponse;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/** MapStruct: Pokemon (core) &lt;-&gt; DTO (doc §7.4). unmappedTargetPolicy=ERROR: si un campo nuevo se
 * agrega al DTO o al modelo y falta mapearlo, falla el build en vez de producir un null silencioso
 * (así se detectó que "stats" no se estaba mapeando de PokemonRequest a Pokemon). */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PokemonDtoMapper {

    PokemonResponse toResponse(Pokemon pokemon);

    PokemonStatsResponse toResponse(PokemonStats stats);

    List<PokemonResponse> toResponseList(List<Pokemon> pokemons);

    @Mapping(target = "id", ignore = true)
    Pokemon toDomain(PokemonRequest request);

    PokemonStats toDomain(PokemonStatsRequest request);
}
