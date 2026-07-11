package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.pokedex_api.controller.mapper.PokemonDtoMapper;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.FavoriteService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FavoriteService favoriteService;
    @MockBean
    private PokemonDtoMapper mapper;
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
    void list_returnsFavoritePokemon() throws Exception {
        when(favoriteService.listFavorites(1L)).thenReturn(List.of(Pokemon.builder().id(25L).name("Pikachu").build()));
        when(mapper.toResponseList(org.mockito.ArgumentMatchers.any())).thenReturn(List.of());

        mockMvc.perform(get("/v1/favorites")).andExpect(status().isOk());
    }

    @Test
    void toggle_returnsFavoritedFlag() throws Exception {
        when(favoriteService.toggleFavorite(1L, 25L)).thenReturn(true);

        mockMvc.perform(post("/v1/favorites/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(true));
    }
}
