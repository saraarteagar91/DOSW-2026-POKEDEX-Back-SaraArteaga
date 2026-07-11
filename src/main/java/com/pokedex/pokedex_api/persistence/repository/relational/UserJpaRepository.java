package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.core.model.Role;
import com.pokedex.pokedex_api.persistence.entity.relational.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.viewedPokemonCount = u.viewedPokemonCount + 1 WHERE u.id = :userId")
    void incrementViewedCount(Long userId);
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    long countByRole(Role role);

    List<UserEntity> findAllByOrderByCreatedAtDesc();
}
