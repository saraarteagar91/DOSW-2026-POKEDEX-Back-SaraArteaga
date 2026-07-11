package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonViewPort;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import com.pokedex.pokedex_api.core.util.PokemonOfDaySelector;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** RF-01..04 (catálogo/búsqueda/filtros), RF-11..13 (CRUD admin), RN-02/06/07, RF-19 (Pokémon del día). */
@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonPersistencePort pokemonPort;
    private final PokemonViewPort pokemonViewPort;
    private final UserPersistencePort userPersistencePort;
    private final BadgeService badgeService;
    private final Clock clock;

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        return pokemonPort.findAll(pageable);
    }

    @Override
    public Page<Pokemon> search(PokemonFilterCriteria criteria, Pageable pageable) {
        return pokemonPort.search(criteria, pageable);
    }

    @Override
    public Pokemon findById(Long id) {
        log.debug("PokéBloom: buscando Pokémon con id {}", id);
        return pokemonPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }

    @Override
    public Pokemon viewDetail(Long id, Long viewerUserId) {
        Pokemon pokemon = findById(id);
        pokemonViewPort.registerView(pokemon.getId(), pokemon.getNationalNumber(), pokemon.getName());
        if (viewerUserId != null) {
            userPersistencePort.incrementViewedCount(viewerUserId);
            badgeService.evaluate(viewerUserId);
        }
        return pokemon;
    }

    @Override
    public Pokemon create(Pokemon pokemon) {
        if (pokemonPort.existsByNationalNumber(pokemon.getNationalNumber())) {
            throw new DuplicateResourceException("Pokemon", "nationalNumber", pokemon.getNationalNumber());
        }
        log.info("PokéBloom: registrando Pokémon {}", pokemon.getName());
        return pokemonPort.save(pokemon.toBuilder().id(null).build());
    }

    @Override
    public Pokemon update(Long id, Pokemon pokemon) {
        Pokemon existing = findById(id);
        Pokemon toSave = pokemon.toBuilder()
                .id(existing.getId())
                .nationalNumber(existing.getNationalNumber())
                .build();
        return pokemonPort.save(toSave);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        if (pokemonPort.isReferencedByTeamOrFavorite(id)) {
            throw new ForbiddenOperationException(
                    "Este Pokémon está en uso en equipos o favoritos; no se puede retirar del catálogo");
        }
        pokemonPort.deleteById(id);
    }

    @Override
    public Pokemon pokemonOfTheDay() {
        List<Pokemon> catalog = pokemonPort.findAllOrderedByNumber();
        if (catalog.isEmpty()) {
            throw new ResourceNotFoundException("Pokemon", "catálogo", "vacío");
        }
        int index = PokemonOfDaySelector.indexForDate(LocalDate.now(clock), catalog.size());
        return catalog.get(index);
    }
}
