package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.pokedex_api.controller.mapper.PokemonDtoMapper;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.PokemonStats;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.security.JwtService;
import com.pokedex.pokedex_api.security.UserDetailsServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PokemonService pokemonService;
    @MockBean
    private PokemonDtoMapper mapper;
    // JwtAuthFilter (un @Component Filter) entra en el escaneo de @WebMvcTest; se mockean sus
    // dependencias para que no intente construir toda la cadena real de seguridad.
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void findById_returns200WithPokemonData() throws Exception {
        Pokemon pikachu = Pokemon.builder().id(25L).nationalNumber(25).name("Pikachu")
                .types(List.of("Electric"))
                .stats(PokemonStats.builder().hp(35).attack(55).defense(40)
                        .specialAttack(50).specialDefense(50).speed(90).build())
                .build();
        var response = new com.pokedex.pokedex_api.controller.dto.response.PokemonResponse(
                25L, 25, "Pikachu", null, null, List.of("Electric"), "Kanto", 1, false, 1, null, null,
                List.of(), List.of(), null);

        when(pokemonService.viewDetail(eq(25L), any())).thenReturn(pikachu);
        when(mapper.toResponse(pikachu)).thenReturn(response);

        mockMvc.perform(get("/v1/pokemon/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu"));
    }

    @Test
    void pokemonOfTheDay_returns200() throws Exception {
        Pokemon bulbasaur = Pokemon.builder().id(1L).nationalNumber(1).name("Bulbasaur").build();
        var response = new com.pokedex.pokedex_api.controller.dto.response.PokemonResponse(
                1L, 1, "Bulbasaur", null, null, List.of(), "Kanto", 1, false, 1, null, null,
                List.of(), List.of(), null);
        when(pokemonService.pokemonOfTheDay()).thenReturn(bulbasaur);
        when(mapper.toResponse(bulbasaur)).thenReturn(response);

        mockMvc.perform(get("/v1/pokemon/of-the-day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bulbasaur"));
    }
}
