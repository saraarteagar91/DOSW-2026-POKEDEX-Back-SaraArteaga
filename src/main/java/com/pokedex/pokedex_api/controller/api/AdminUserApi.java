package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.request.UpdateUserRoleRequest;
import com.pokedex.pokedex_api.controller.dto.request.UpdateUserStatusRequest;
import com.pokedex.pokedex_api.controller.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin - Users", description = "RF-14, RN-09: gestión de cuentas de la comunidad. Solo ADMIN.")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/admin/users")
public interface AdminUserApi {

    @Operation(summary = "Listar cuentas de la comunidad")
    @GetMapping
    ResponseEntity<List<UserResponse>> listAll();

    @Operation(summary = "Cambiar el rol de una cuenta", description = "No se puede degradar al último ADMIN")
    @PatchMapping("/{id}/role")
    ResponseEntity<UserResponse> changeRole(@PathVariable Long id, @Valid @RequestBody UpdateUserRoleRequest request);

    @Operation(summary = "Habilitar o deshabilitar una cuenta", description = "No se puede deshabilitar al último ADMIN")
    @PatchMapping("/{id}/status")
    ResponseEntity<UserResponse> changeStatus(@PathVariable Long id,
                                               @Valid @RequestBody UpdateUserStatusRequest request);
}
