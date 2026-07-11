package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonFilterCriteria;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.AbilityEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.MoveEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.PokemonStatsEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.RegionEntity;
import com.pokedex.pokedex_api.persistence.entity.relational.TypeEntity;
import com.pokedex.pokedex_api.persistence.mapper.PokemonPersistenceMapper;
import com.pokedex.pokedex_api.persistence.repository.relational.AbilityJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.FavoriteJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.MoveJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.PokemonStatsJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.RegionJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.TeamJpaRepository;
import com.pokedex.pokedex_api.persistence.repository.relational.TypeJpaRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa el puerto definido en core resolviendo relaciones (tipos, región, habilidades, movimientos)
 * contra sus catálogos. {@code @Transactional} mantiene la sesión de Hibernate abierta mientras el mapper
 * recorre las colecciones LAZY (open-in-view está deshabilitado a propósito, doc §5.3).
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PokemonPersistenceAdapter implements PokemonPersistencePort {

    private final PokemonJpaRepository pokemonRepository;
    private final PokemonStatsJpaRepository statsRepository;
    private final TypeJpaRepository typeRepository;
    private final RegionJpaRepository regionRepository;
    private final AbilityJpaRepository abilityRepository;
    private final MoveJpaRepository moveRepository;
    private final FavoriteJpaRepository favoriteRepository;
    private final TeamJpaRepository teamRepository;
    private final PokemonPersistenceMapper mapper;

    @Override
    public Optional<Pokemon> findById(Long id) {
        return pokemonRepository.findWithDetailsById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Pokemon> findByNationalNumber(Integer nationalNumber) {
        return pokemonRepository.findByNationalNumber(nationalNumber).map(mapper::toDomain);
    }

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        return pokemonRepository.findAllWithTypes(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Pokemon> search(PokemonFilterCriteria criteria, Pageable pageable) {
        return pokemonRepository.findAll(PokemonSpecifications.fromCriteria(criteria), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Pokemon> findAllOrderedByNumber() {
        return pokemonRepository.findAllByOrderByNationalNumberAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNationalNumber(Integer nationalNumber) {
        return pokemonRepository.existsByNationalNumber(nationalNumber);
    }

    @Override
    public boolean isReferencedByTeamOrFavorite(Long pokemonId) {
        return favoriteRepository.existsByPokemonId(pokemonId) || teamRepository.existsPokemonInAnyTeam(pokemonId);
    }

    @Override
    @Transactional
    public Pokemon save(Pokemon pokemon) {
        PokemonEntity entity = loadOrCreate(pokemon.getId());

        entity.setNationalNumber(pokemon.getNationalNumber());
        entity.setName(pokemon.getName());
        entity.setDescription(pokemon.getDescription());
        entity.setImageUrl(pokemon.getImageUrl());
        entity.setGeneration(pokemon.getGeneration());
        entity.setHasMega(Boolean.TRUE.equals(pokemon.getHasMega()));
        entity.setEvolutionStage(pokemon.getEvolutionStage() == null ? 1 : pokemon.getEvolutionStage());
        entity.setEvolvesFromId(pokemon.getEvolvesFromId());
        entity.setColor(pokemon.getColor());
        entity.setRegion(resolveRegion(pokemon.getRegion()));

        entity.getTypes().clear();
        entity.getTypes().addAll(resolveTypes(pokemon.getTypes()));
        entity.getAbilities().clear();
        entity.getAbilities().addAll(resolveAbilities(pokemon.getAbilities()));
        entity.getMoves().clear();
        entity.getMoves().addAll(resolveMoves(pokemon.getMoves()));

        PokemonEntity saved = pokemonRepository.save(entity);
        applyStats(saved, pokemon);

        return mapper.toDomain(pokemonRepository.findWithDetailsById(saved.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        pokemonRepository.deleteById(id);
    }

    private PokemonEntity loadOrCreate(Long id) {
        if (id == null) {
            return PokemonEntity.builder()
                    .types(new LinkedHashSet<>())
                    .abilities(new LinkedHashSet<>())
                    .moves(new LinkedHashSet<>())
                    .build();
        }
        return pokemonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }

    private void applyStats(PokemonEntity pokemon, Pokemon source) {
        if (source.getStats() == null) {
            return;
        }
        PokemonStatsEntity stats = pokemon.getStats() != null
                ? pokemon.getStats()
                : PokemonStatsEntity.builder().pokemon(pokemon).build();
        stats.setPokemon(pokemon);
        stats.setHp(source.getStats().getHp());
        stats.setAttack(source.getStats().getAttack());
        stats.setDefense(source.getStats().getDefense());
        stats.setSpecialAttack(source.getStats().getSpecialAttack());
        stats.setSpecialDefense(source.getStats().getSpecialDefense());
        stats.setSpeed(source.getStats().getSpeed());
        statsRepository.save(stats);
        // @OneToOne(mappedBy) es el lado inverso: Hibernate no reasocia solo la referencia en memoria
        // de una entidad recién creada dentro de la misma sesión. Sin este set explícito, el reload
        // posterior (mismo persistence context) devuelve el pokemon con stats=null pese al INSERT real.
        pokemon.setStats(stats);
    }

    private RegionEntity resolveRegion(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return regionRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> regionRepository.save(RegionEntity.builder().name(name).build()));
    }

    private List<TypeEntity> resolveTypes(List<String> names) {
        return names == null ? List.of() : names.stream()
                .map(name -> typeRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> typeRepository.save(TypeEntity.builder().name(name).build())))
                .toList();
    }

    private List<AbilityEntity> resolveAbilities(List<String> names) {
        return names == null ? List.of() : names.stream()
                .map(name -> abilityRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> abilityRepository.save(AbilityEntity.builder().name(name).build())))
                .toList();
    }

    private List<MoveEntity> resolveMoves(List<String> names) {
        return names == null ? List.of() : names.stream()
                .map(name -> moveRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> moveRepository.save(MoveEntity.builder().name(name).build())))
                .toList();
    }
}
