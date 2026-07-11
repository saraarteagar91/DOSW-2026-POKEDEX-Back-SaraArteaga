package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.ShareLinkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareLinkJpaRepository extends JpaRepository<ShareLinkEntity, Long> {
    Optional<ShareLinkEntity> findByToken(String token);
}
