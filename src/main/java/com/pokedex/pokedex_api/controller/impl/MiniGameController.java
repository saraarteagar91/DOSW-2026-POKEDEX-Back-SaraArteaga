package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.MiniGameApi;
import com.pokedex.pokedex_api.controller.dto.request.MiniGameAnswerRequest;
import com.pokedex.pokedex_api.controller.dto.response.MiniGameAnswerResponse;
import com.pokedex.pokedex_api.controller.dto.response.MiniGameRoundResponse;
import com.pokedex.pokedex_api.controller.mapper.MiniGameDtoMapper;
import com.pokedex.pokedex_api.core.service.interfaces.MiniGameService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MiniGameController implements MiniGameApi {

    private final MiniGameService miniGameService;
    private final MiniGameDtoMapper mapper;

    @Override
    public ResponseEntity<MiniGameRoundResponse> start(AuthenticatedUser principal) {
        return ResponseEntity.ok(mapper.toResponse(miniGameService.startRound(principal.getUserId())));
    }

    @Override
    public ResponseEntity<MiniGameAnswerResponse> answer(MiniGameAnswerRequest request, AuthenticatedUser principal) {
        var result = miniGameService.answer(principal.getUserId(), request.guess());
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
