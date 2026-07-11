package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.ShareLink;
import java.util.Optional;

public interface ShareLinkPersistencePort {
    Optional<ShareLink> findByToken(String token);

    ShareLink save(ShareLink shareLink);
}
