package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.UserAdminService;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-14 / RN-09: solo ADMIN gestiona cuentas. E1: no se puede afectar al último administrador. */
@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserPersistencePort userPersistencePort;

    @Override
    public List<User> listAll() {
        return userPersistencePort.findAll();
    }

    @Override
    public User changeRole(Long userId, Role newRole) {
        User user = findOrThrow(userId);
        if (user.getRole() == Role.ADMIN && newRole != Role.ADMIN) {
            assertNotLastAdmin();
        }
        return userPersistencePort.save(user.toBuilder().role(newRole).build());
    }

    @Override
    public User setEnabled(Long userId, boolean enabled) {
        User user = findOrThrow(userId);
        if (!enabled && user.getRole() == Role.ADMIN) {
            assertNotLastAdmin();
        }
        return userPersistencePort.save(user.toBuilder().enabled(enabled).build());
    }

    private User findOrThrow(Long userId) {
        return userPersistencePort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
    }

    private void assertNotLastAdmin() {
        if (userPersistencePort.countByRole(Role.ADMIN) <= 1) {
            throw new ForbiddenOperationException(
                    "No es posible afectar al último administrador de la comunidad");
        }
    }
}
