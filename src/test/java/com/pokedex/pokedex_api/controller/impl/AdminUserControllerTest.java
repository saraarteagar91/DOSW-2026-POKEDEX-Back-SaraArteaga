package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokedex_api.controller.dto.request.UpdateUserRoleRequest;
import com.pokedex.pokedex_api.controller.mapper.UserDtoMapper;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.UserAdminService;
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

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserAdminService userAdminService;
    @MockBean
    private UserDtoMapper mapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void authenticate() {
        AuthenticatedUser principal = new AuthenticatedUser(User.builder()
                .id(1L).username("admin").email("admin@pokebloom.com")
                .role(Role.ADMIN).provider(AuthProvider.LOCAL).enabled(true).build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listAll_returns200() throws Exception {
        when(userAdminService.listAll()).thenReturn(List.of());
        mockMvc.perform(get("/v1/admin/users")).andExpect(status().isOk());
    }

    @Test
    void changeRole_withValidBody_returns200() throws Exception {
        User admin = User.builder().id(2L).username("otra").email("otra@pokebloom.com")
                .role(Role.ADMIN).provider(AuthProvider.LOCAL).enabled(true).build();
        when(userAdminService.changeRole(2L, Role.ADMIN)).thenReturn(admin);

        mockMvc.perform(patch("/v1/admin/users/2/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateUserRoleRequest(Role.ADMIN))))
                .andExpect(status().isOk());
    }
}
