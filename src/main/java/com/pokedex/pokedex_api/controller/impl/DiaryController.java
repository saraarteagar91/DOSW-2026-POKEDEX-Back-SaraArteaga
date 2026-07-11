package com.pokedex.pokedex_api.controller.impl;

import com.pokedex.pokedex_api.controller.api.DiaryApi;
import com.pokedex.pokedex_api.controller.dto.request.DiaryNoteRequest;
import com.pokedex.pokedex_api.controller.dto.response.DiaryNoteResponse;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex_api.core.model.DiaryNote;
import com.pokedex.pokedex_api.core.service.interfaces.DiaryService;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiaryController implements DiaryApi {

    private final DiaryService diaryService;

    @Override
    public ResponseEntity<DiaryNoteResponse> getNote(Long pokemonId, AuthenticatedUser principal) {
        DiaryNote note = diaryService.getNote(principal.getUserId(), pokemonId)
                .orElseThrow(() -> new ResourceNotFoundException("DiaryNote", "pokemonId", pokemonId));
        return ResponseEntity.ok(toResponse(note));
    }

    @Override
    public ResponseEntity<DiaryNoteResponse> upsertNote(Long pokemonId, DiaryNoteRequest request,
                                                          AuthenticatedUser principal) {
        DiaryNote note = diaryService.upsertNote(principal.getUserId(), pokemonId, request.text());
        return ResponseEntity.ok(toResponse(note));
    }

    private DiaryNoteResponse toResponse(DiaryNote note) {
        return new DiaryNoteResponse(note.getPokemonId(), note.getText(), note.getUpdatedAt());
    }
}
