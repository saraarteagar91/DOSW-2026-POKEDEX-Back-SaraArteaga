package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.TeamSynergy;
import java.util.List;

public interface TeamService {
    List<Team> listByUser(Long userId);

    Team create(Long userId, String name, List<Long> pokemonIds);

    Team update(Long teamId, Long userId, String name, List<Long> pokemonIds);

    void delete(Long teamId, Long userId);

    TeamSynergy synergyOf(Long teamId, Long userId);

    /** Sin validar dueño: solo para resolver enlaces públicos de compartir (RF-17). */
    Team getById(Long teamId);
}
