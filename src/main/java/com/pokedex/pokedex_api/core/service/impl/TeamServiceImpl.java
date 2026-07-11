package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import com.pokedex.pokedex_api.core.service.interfaces.BadgeService;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.TeamPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.TeamService;
import com.pokedex.pokedex_api.core.util.TypeEffectivenessUtil;
import com.pokedex.pokedex_api.core.validator.TeamValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-08 (equipos + sinergia), RF-09 (administrar mis equipos), RN-04/RN-05. */
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamPersistencePort teamPort;
    private final PokemonService pokemonService;
    private final TeamValidator teamValidator;
    private final BadgeService badgeService;

    @Override
    public List<Team> listByUser(Long userId) {
        return teamPort.findByUserId(userId);
    }

    @Override
    public Team create(Long userId, String name, List<Long> pokemonIds) {
        teamValidator.validate(name, pokemonIds);
        if (teamPort.existsByUserIdAndName(userId, name)) {
            throw new DuplicateResourceException("Equipo", "name", name);
        }
        pokemonIds.forEach(pokemonService::findById);
        Team saved = teamPort.save(Team.builder().userId(userId).name(name).pokemonIds(pokemonIds).build());
        badgeService.evaluate(userId);
        return saved;
    }

    @Override
    public Team update(Long teamId, Long userId, String name, List<Long> pokemonIds) {
        Team team = requireOwnedTeam(teamId, userId);
        teamValidator.validate(name, pokemonIds);
        pokemonIds.forEach(pokemonService::findById);
        Team saved = teamPort.save(team.toBuilder().name(name).pokemonIds(pokemonIds).build());
        badgeService.evaluate(userId);
        return saved;
    }

    @Override
    public void delete(Long teamId, Long userId) {
        requireOwnedTeam(teamId, userId);
        teamPort.deleteById(teamId);
    }

    @Override
    public TeamSynergy synergyOf(Long teamId, Long userId) {
        Team team = requireOwnedTeam(teamId, userId);
        List<Pokemon> members = team.getPokemonIds().stream().map(pokemonService::findById).toList();
        return TypeEffectivenessUtil.computeSynergy(members);
    }

    @Override
    public Team getById(Long teamId) {
        return teamPort.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));
    }

    private Team requireOwnedTeam(Long teamId, Long userId) {
        Team team = teamPort.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));
        if (!team.getUserId().equals(userId)) {
            throw new ForbiddenOperationException("Este equipo no pertenece a la entrenadora autenticada");
        }
        return team;
    }
}
