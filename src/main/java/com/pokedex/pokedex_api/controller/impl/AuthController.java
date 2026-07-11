package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.AuthApi;
import com.pokedex.pokedex_api.controller.dto.request.LoginRequest;
import com.pokedex.pokedex_api.controller.dto.request.RegisterRequest;
import com.pokedex.pokedex_api.controller.dto.response.TokenResponse;
import com.pokedex.pokedex_api.controller.mapper.UserDtoMapper;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.AuthService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import com.pokedex.pokedex_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDtoMapper userDtoMapper;

    @Override
    public ResponseEntity<TokenResponse> register(RegisterRequest request) {
        User user = authService.register(request.username(), request.email(), request.password(),
                request.avatarUrl());
        return ResponseEntity.status(201).body(toTokenResponse(user));
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        User user = authService.authenticate(request.email(), request.password());
        return ResponseEntity.ok(toTokenResponse(user));
    }

    private TokenResponse toTokenResponse(User user) {
        String token = jwtService.generateToken(new AuthenticatedUser(user));
        return new TokenResponse(token, userDtoMapper.toResponse(user));
    }
}
