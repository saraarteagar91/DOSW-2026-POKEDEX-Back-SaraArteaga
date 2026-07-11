package com.pokedex.pokedex_api.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokedex_api.controller.dto.request.LoginRequest;
import com.pokedex.pokedex_api.controller.dto.request.RegisterRequest;
import com.pokedex.pokedex_api.controller.dto.response.UserResponse;
import com.pokedex.pokedex_api.controller.mapper.UserDtoMapper;
import com.pokedex.pokedex_api.core.model.AuthProvider;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.AuthService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import com.pokedex.pokedex_api.security.JwtService;
import com.pokedex.pokedex_api.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDtoMapper userDtoMapper;
    // JwtAuthFilter (un @Component Filter) entra en el escaneo de @WebMvcTest; se mockea su
    // dependencia UserDetailsServiceImpl para que no intente construir toda la cadena real.
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void register_withValidBody_returns201() throws Exception {
        User user = User.builder().id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true).build();
        when(authService.register(anyString(), anyString(), anyString(), any())).thenReturn(user);
        when(jwtService.generateToken(any(AuthenticatedUser.class))).thenReturn("fake-jwt");
        when(userDtoMapper.toResponse(user)).thenReturn(
                new UserResponse(1L, "sara", "sara@pokebloom.com", Role.TRAINER, AuthProvider.LOCAL, null, true, null));

        RegisterRequest request = new RegisterRequest("sara", "sara@pokebloom.com", "clave1234", null);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("fake-jwt"))
                .andExpect(jsonPath("$.user.username").value("sara"));
    }

    @Test
    void register_withInvalidBody_returns400() throws Exception {
        RegisterRequest invalid = new RegisterRequest("", "not-an-email", "123", null);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withValidCredentials_returns200() throws Exception {
        User user = User.builder().id(1L).username("sara").email("sara@pokebloom.com")
                .role(Role.TRAINER).provider(AuthProvider.LOCAL).enabled(true).build();
        when(authService.authenticate(anyString(), anyString())).thenReturn(user);
        when(jwtService.generateToken(any(AuthenticatedUser.class))).thenReturn("fake-jwt");
        when(userDtoMapper.toResponse(user)).thenReturn(
                new UserResponse(1L, "sara", "sara@pokebloom.com", Role.TRAINER, AuthProvider.LOCAL, null, true, null));

        LoginRequest request = new LoginRequest("sara@pokebloom.com", "clave1234");

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"));
    }
}
