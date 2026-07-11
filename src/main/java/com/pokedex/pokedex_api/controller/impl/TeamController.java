package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.TeamApi;
import com.pokedex.pokedex_api.controller.dto.request.TeamRequest;
import com.pokedex.pokedex_api.controller.dto.response.TeamResponse;
import com.pokedex.pokedex_api.controller.dto.response.TeamSynergyResponse;
import com.pokedex.pokedex_api.controller.mapper.TeamDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.TeamService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;
    private final TeamDtoMapper mapper;

    @Override
    public ResponseEntity<List<TeamResponse>> listMine(AuthenticatedUser principal) {
        var teams = teamService.listByUser(principal.getUserId()).stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(teams);
    }

    @Override
    public ResponseEntity<TeamResponse> create(TeamRequest request, AuthenticatedUser principal) {
        var team = teamService.create(principal.getUserId(), request.name(), request.pokemonIds());
        return ResponseEntity.status(201).body(mapper.toResponse(team));
    }

    @Override
    public ResponseEntity<TeamResponse> update(Long id, TeamRequest request, AuthenticatedUser principal) {
        var team = teamService.update(id, principal.getUserId(), request.name(), request.pokemonIds());
        return ResponseEntity.ok(mapper.toResponse(team));
    }

    @Override
    public ResponseEntity<Void> delete(Long id, AuthenticatedUser principal) {
        teamService.delete(id, principal.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TeamSynergyResponse> synergy(Long id, AuthenticatedUser principal) {
        return ResponseEntity.ok(mapper.toResponse(teamService.synergyOf(id, principal.getUserId())));
    }
}
