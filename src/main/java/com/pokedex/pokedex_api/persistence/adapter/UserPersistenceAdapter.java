package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.UserPersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex_api.persistence.mapper.UserPersistenceMapper;
import com.pokedex.pokedex_api.persistence.repository.relational.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserJpaRepository repository;
    private final UserPersistenceMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameIgnoreCase(username).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public long countByRole(Role role) {
        return repository.countByRole(role);
    }

    @Override
    public List<User> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public User save(User user) {
        UserEntity entity = user.getId() != null
                ? repository.findById(user.getId()).orElseGet(() -> mapper.toEntity(user))
                : mapper.toEntity(user);
        UserEntity toPersist = UserEntity.builder()
                .id(entity.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .provider(user.getProvider())
                .avatarUrl(user.getAvatarUrl())
                .enabled(user.getEnabled())
                .viewedPokemonCount(user.getViewedPokemonCount() == null ? 0 : user.getViewedPokemonCount())
                .createdAt(entity.getCreatedAt())
                .build();
        return mapper.toDomain(repository.save(toPersist));
    }

    @Override
    public void incrementViewedCount(Long userId) {
        repository.incrementViewedCount(userId);
    }
}
