package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.PokemonApi;
import com.pokedex.pokedex_api.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex_api.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex_api.controller.mapper.PokemonDtoMapper;
import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {

    private final PokemonService pokemonService;
    private final PokemonDtoMapper mapper;

    @Override
    public ResponseEntity<Page<PokemonResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(pokemonService.findAll(pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Page<PokemonResponse>> search(String search, String region, String type,
            Integer generation, String ability, String move, Boolean hasMega, String color,
            Integer evolutionStage, Integer minTotalStats, Integer maxTotalStats, Pageable pageable) {
        PokemonFilterCriteria criteria = new PokemonFilterCriteria(search, region, type, generation, ability,
                move, hasMega, color, evolutionStage, minTotalStats, maxTotalStats);
        return ResponseEntity.ok(pokemonService.search(criteria, pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<PokemonResponse> pokemonOfTheDay() {
        return ResponseEntity.ok(mapper.toResponse(pokemonService.pokemonOfTheDay()));
    }

    @Override
    public ResponseEntity<PokemonResponse> findById(Long id, AuthenticatedUser principal) {
        Long viewerId = principal == null ? null : principal.getUserId();
        return ResponseEntity.ok(mapper.toResponse(pokemonService.viewDetail(id, viewerId)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        var created = pokemonService.create(mapper.toDomain(request));
        return ResponseEntity.status(201).body(mapper.toResponse(created));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> update(Long id, PokemonRequest request) {
        var updated = pokemonService.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(Long id) {
        pokemonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
