package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareResolution;
import com.pokedex.pokedex_api.core.model.ShareType;

public interface ShareService {
    ShareLink createLink(Long userId, ShareType type, Long refId);

    ShareResolution resolve(String token);
}
