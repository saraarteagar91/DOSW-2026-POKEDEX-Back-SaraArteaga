package com.pokedex.pokedex_api.core.service.interfaces;

import com.pokedex.pokedex_api.core.model.MiniGameAnswerResult;
import com.pokedex.pokedex_api.core.model.MiniGameRound;

public interface MiniGameService {
    MiniGameRound startRound(Long userId);

    MiniGameAnswerResult answer(Long userId, String guess);
}
