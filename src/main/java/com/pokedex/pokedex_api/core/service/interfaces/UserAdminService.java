package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import java.util.List;

/** RF-14: gestión de cuentas de la comunidad, solo ADMIN (RN-09). */
public interface UserAdminService {
    List<User> listAll();

    User changeRole(Long userId, Role newRole);

    User setEnabled(Long userId, boolean enabled);
}
