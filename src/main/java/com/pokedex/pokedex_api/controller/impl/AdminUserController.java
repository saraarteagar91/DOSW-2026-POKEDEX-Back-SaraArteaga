package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.AdminUserApi;
import com.pokedex.pokedex_api.controller.dto.request.UpdateUserRoleRequest;
import com.pokedex.pokedex_api.controller.dto.request.UpdateUserStatusRequest;
import com.pokedex.pokedex_api.controller.dto.response.UserResponse;
import com.pokedex.pokedex_api.controller.mapper.UserDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.UserAdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController implements AdminUserApi {

    private final UserAdminService userAdminService;
    private final UserDtoMapper mapper;

    @Override
    public ResponseEntity<List<UserResponse>> listAll() {
        return ResponseEntity.ok(userAdminService.listAll().stream().map(mapper::toResponse).toList());
    }

    @Override
    public ResponseEntity<UserResponse> changeRole(Long id, UpdateUserRoleRequest request) {
        return ResponseEntity.ok(mapper.toResponse(userAdminService.changeRole(id, request.role())));
    }

    @Override
    public ResponseEntity<UserResponse> changeStatus(Long id, UpdateUserStatusRequest request) {
        return ResponseEntity.ok(mapper.toResponse(userAdminService.setEnabled(id, request.enabled())));
    }
}
