package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.pokedex_api.controller.mapper.StatsDtoMapper;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.CommunityStats;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.StatsService;
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

@WebMvcTest(StatsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatsService statsService;
    @MockBean
    private StatsDtoMapper mapper;
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
    void communityStats_returns200() throws Exception {
        when(statsService.communityStats()).thenReturn(new CommunityStats(List.of(), List.of(), 0L, 0L));
        mockMvc.perform(get("/v1/stats")).andExpect(status().isOk());
    }
}
