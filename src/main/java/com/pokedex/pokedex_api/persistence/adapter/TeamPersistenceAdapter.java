package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.TeamEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.TeamJpaRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamPersistenceAdapter implements TeamPersistencePort {

    private final TeamJpaRepository repository;

    @Override
    public Optional<Team> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Team> findByUserId(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByUserIdAndName(Long userId, String name) {
        return repository.existsByUserIdAndNameIgnoreCase(userId, name);
    }

    @Override
    public long countByUserId(Long userId) {
        return repository.countByUserId(userId);
    }

    @Override
    @Transactional
    public Team save(Team team) {
        TeamEntity entity = team.getId() != null
                ? repository.findById(team.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team", "id", team.getId()))
                : TeamEntity.builder().userId(team.getUserId()).pokemonIds(new ArrayList<>()).build();

        entity.setName(team.getName());
        entity.getPokemonIds().clear();
        if (team.getPokemonIds() != null) {
            entity.getPokemonIds().addAll(team.getPokemonIds());
        }

        return toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Map<Long, Long> countChoicesByPokemon() {
        Map<Long, Long> result = new LinkedHashMap<>();
        repository.countChoicesByPokemon().forEach(row -> result.put(row.getPokemonId(), row.getTotal()));
        return result;
    }

    @Override
    public long countAll() {
        return repository.count();
    }

    private Team toDomain(TeamEntity entity) {
        return Team.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .pokemonIds(List.copyOf(entity.getPokemonIds()))
                .build();
    }
}
