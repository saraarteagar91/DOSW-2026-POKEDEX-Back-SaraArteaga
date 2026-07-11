package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokedex_api.controller.dto.request.TeamRequest;
import com.pokedex.pokedex_api.controller.dto.response.TeamResponse;
import com.pokedex.pokedex_api.controller.mapper.TeamDtoMapper;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.TeamService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import com.pokedex.pokedex_api.security.JwtService;
import com.pokedex.pokedex_api.security.UserDetailsServiceImpl;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * addFilters = false desactiva JwtAuthFilter, así que el SecurityContext se establece directamente
 * (no vía la petición) — con los filtros apagados, el postprocessor .with(authentication(...)) no
 * alcanza a SecurityContextHolder porque eso normalmente lo hace SecurityContextHolderFilter.
 */
@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TeamService teamService;
    @MockBean
    private TeamDtoMapper mapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void authenticate() {
        AuthenticatedUser principal = new AuthenticatedUser(User.builder()
                .id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true).build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listMine_returnsTeamsForAuthenticatedUser() throws Exception {
        Team team = Team.builder().id(1L).userId(1L).name("Equipo").pokemonIds(List.of(1L)).build();
        when(teamService.listByUser(1L)).thenReturn(List.of(team));
        when(mapper.toResponse(team)).thenReturn(new TeamResponse(1L, 1L, "Equipo", List.of(1L)));

        mockMvc.perform(get("/v1/teams")).andExpect(status().isOk());
    }

    @Test
    void create_withValidBody_returns201() throws Exception {
        Team team = Team.builder().id(1L).userId(1L).name("Equipo").pokemonIds(List.of(1L)).build();
        when(teamService.create(anyLong(), anyString(), any())).thenReturn(team);
        when(mapper.toResponse(team)).thenReturn(new TeamResponse(1L, 1L, "Equipo", List.of(1L)));

        TeamRequest request = new TeamRequest("Equipo", List.of(1L));

        mockMvc.perform(post("/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_withEmptyPokemonList_returns400() throws Exception {
        TeamRequest invalid = new TeamRequest("Equipo", List.of());

        mockMvc.perform(post("/v1/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
