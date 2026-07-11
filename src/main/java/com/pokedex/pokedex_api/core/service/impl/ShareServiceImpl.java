package com.pokedex.pokedex_api.core.service.impl;

import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.Pokemon;
import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareResolution;
import com.pokedex.pokedex_api.core.model.ShareType;
import com.pokedex.pokedex_api.core.model.Team;
import com.pokedex.pokedex_api.core.service.interfaces.PokemonService;
import com.pokedex.pokedex_api.core.service.interfaces.ShareLinkPersistencePort;
import com.pokedex.pokedex_api.core.service.interfaces.ShareService;
import com.pokedex.pokedex_api.core.service.interfaces.TeamService;
import com.pokedex.pokedex_api.core.util.TypeEffectivenessUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * RF-17: el backend genera el enlace público y los datos estructurados de la tarjeta; la imagen
 * descargable la construye el frontend a partir de esos datos (decisión de alcance documentada en el README).
 */
@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    private final ShareLinkPersistencePort shareLinkPort;
    private final PokemonService pokemonService;
    private final TeamService teamService;

    @Override
    public ShareLink createLink(Long userId, ShareType type, Long refId) {
        if (type == ShareType.POKEMON) {
            pokemonService.findById(refId);
        } else {
            Team team = teamService.getById(refId);
            if (!team.getUserId().equals(userId)) {
                throw new ForbiddenOperationException("Solo puedes compartir tus propios equipos");
            }
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        return shareLinkPort.save(ShareLink.builder()
                .token(token)
                .type(type)
                .refId(refId)
                .ownerUserId(userId)
                .build());
    }

    @Override
    public ShareResolution resolve(String token) {
        ShareLink link = shareLinkPort.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("ShareLink", "token", token));

        if (link.getType() == ShareType.POKEMON) {
            Pokemon pokemon = pokemonService.findById(link.getRefId());
            return new ShareResolution(ShareType.POKEMON, pokemon, null, null, null);
        }

        Team team = teamService.getById(link.getRefId());
        var members = team.getPokemonIds().stream().map(pokemonService::findById).toList();
        return new ShareResolution(ShareType.TEAM, null, team.getName(), members,
                TypeEffectivenessUtil.computeSynergy(members));
    }
}
