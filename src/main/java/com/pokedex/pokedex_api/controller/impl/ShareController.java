package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.ShareApi;
import com.pokedex.pokedex_api.controller.dto.request.ShareRequest;
import com.pokedex.pokedex_api.controller.dto.response.ShareLinkResponse;
import com.pokedex.pokedex_api.controller.dto.response.ShareResolutionResponse;
import com.pokedex.pokedex_api.controller.mapper.ShareDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.ShareService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShareController implements ShareApi {

    private final ShareService shareService;
    private final ShareDtoMapper mapper;

    @Override
    public ResponseEntity<ShareLinkResponse> create(ShareRequest request, AuthenticatedUser principal) {
        var link = shareService.createLink(principal.getUserId(), request.type(), request.refId());
        return ResponseEntity.status(201).body(mapper.toResponse(link));
    }

    @Override
    public ResponseEntity<ShareResolutionResponse> resolve(String token) {
        return ResponseEntity.ok(mapper.toResponse(shareService.resolve(token)));
    }
}
