package com.pokedex.pokedex_api.persistence.adapter;

import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.service.interfaces.ShareLinkPersistencePort;
import com.pokedex.pokedex_api.persistence.entity.relational.ShareLinkEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.ShareLinkJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShareLinkPersistenceAdapter implements ShareLinkPersistencePort {

    private final ShareLinkJpaRepository repository;

    @Override
    public Optional<ShareLink> findByToken(String token) {
        return repository.findByToken(token).map(this::toDomain);
    }

    @Override
    public ShareLink save(ShareLink shareLink) {
        ShareLinkEntity entity = ShareLinkEntity.builder()
                .token(shareLink.getToken())
                .type(shareLink.getType())
                .refId(shareLink.getRefId())
                .ownerUserId(shareLink.getOwnerUserId())
                .build();
        return toDomain(repository.save(entity));
    }

    private ShareLink toDomain(ShareLinkEntity entity) {
        return ShareLink.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .type(entity.getType())
                .refId(entity.getRefId())
                .ownerUserId(entity.getOwnerUserId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
