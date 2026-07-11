package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokedex_api.controller.dto.request.ShareRequest;
import com.pokedex.pokedex_api.controller.mapper.ShareDtoMapper;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareType;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.ShareService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import com.pokedex.pokedex_api.security.JwtService;
import com.pokedex.pokedex_api.security.UserDetailsServiceImpl;
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

@WebMvcTest(ShareController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShareControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShareService shareService;
    @MockBean
    private ShareDtoMapper mapper;
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
    void create_returns201() throws Exception {
        when(shareService.createLink(1L, ShareType.POKEMON, 1L)).thenReturn(
                ShareLink.builder().id(1L).token("abc").type(ShareType.POKEMON).refId(1L).ownerUserId(1L).build());

        mockMvc.perform(post("/v1/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShareRequest(ShareType.POKEMON, 1L))))
                .andExpect(status().isCreated());
    }

    @Test
    void resolve_isPublic_returns200() throws Exception {
        when(shareService.resolve("abc")).thenReturn(
                new com.pokedex.pokedex_api.core.model.ShareResolution(ShareType.POKEMON,
                        com.pokedex.pokedex_api.core.model.Pokemon.builder().id(1L).build(), null, null, null));

        mockMvc.perform(get("/v1/share/abc")).andExpect(status().isOk());
    }
}
