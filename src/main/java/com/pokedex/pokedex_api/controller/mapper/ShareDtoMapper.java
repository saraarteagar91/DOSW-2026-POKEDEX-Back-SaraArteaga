package com.pokedex.pokedex_api.controller.mapper;

import com.pokedex.pokedex_api.controller.dto.response.ShareLinkResponse;
import com.pokedex.pokedex_api.controller.dto.response.ShareResolutionResponse;
import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareResolution;
import com.pokedex.pokedex_api.core.model.ShareType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShareDtoMapper {

    private final PokemonDtoMapper pokemonDtoMapper;
    private final TeamDtoMapper teamDtoMapper;

    @Value("${app.share.base-url:http://localhost:8080/api/v1/share}")
    private String shareBaseUrl;

    public ShareLinkResponse toResponse(ShareLink link) {
        return new ShareLinkResponse(link.getToken(), shareBaseUrl + "/" + link.getToken());
    }

    public ShareResolutionResponse toResponse(ShareResolution resolution) {
        boolean isTeam = resolution.type() == ShareType.TEAM;
        return new ShareResolutionResponse(
                resolution.type(),
                isTeam ? null : pokemonDtoMapper.toResponse(resolution.pokemon()),
                isTeam ? resolution.teamName() : null,
                isTeam ? pokemonDtoMapper.toResponseList(resolution.teamPokemon()) : null,
                isTeam ? teamDtoMapper.toResponse(resolution.teamSynergy()) : null);
    }
}
