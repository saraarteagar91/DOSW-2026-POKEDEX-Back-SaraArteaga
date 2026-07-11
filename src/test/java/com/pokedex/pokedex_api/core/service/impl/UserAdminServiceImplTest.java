package com.pokedex.pokedex_api.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceImplTest {

    @Mock
    private UserPersistencePort userPersistencePort;
    @InjectMocks
    private UserAdminServiceImpl service;

    @Test
    void changeRole_whenDemotingLastAdmin_throwsForbidden() {
        User admin = User.builder().id(1L).role(Role.ADMIN).enabled(true).build();
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(admin));
        when(userPersistencePort.countByRole(Role.ADMIN)).thenReturn(1L);

        assertThrows(ForbiddenOperationException.class, () -> service.changeRole(1L, Role.TRAINER));
    }

    @Test
    void changeRole_whenOtherAdminsExist_allowsDemotion() {
        User admin = User.builder().id(1L).role(Role.ADMIN).enabled(true).build();
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(admin));
        when(userPersistencePort.countByRole(Role.ADMIN)).thenReturn(2L);
        when(userPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.changeRole(1L, Role.TRAINER);

        assertThat(result.getRole()).isEqualTo(Role.TRAINER);
    }

    @Test
    void setEnabled_whenDisablingLastAdmin_throwsForbidden() {
        User admin = User.builder().id(1L).role(Role.ADMIN).enabled(true).build();
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(admin));
        when(userPersistencePort.countByRole(Role.ADMIN)).thenReturn(1L);

        assertThrows(ForbiddenOperationException.class, () -> service.setEnabled(1L, false));
    }

    @Test
    void setEnabled_forTrainer_alwaysAllowed() {
        User trainer = User.builder().id(2L).role(Role.TRAINER).enabled(true).build();
        when(userPersistencePort.findById(2L)).thenReturn(Optional.of(trainer));
        when(userPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.setEnabled(2L, false);

        assertThat(result.getEnabled()).isFalse();
    }
}
