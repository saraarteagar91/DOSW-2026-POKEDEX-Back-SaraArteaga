package com.pokedex.pokedex_api.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.pokedex_api.core.model.Badge;
import com.pokedex.pokedex_api.core.model.BadgeCriteria;
import com.pokedex.pokedex_api.core.model.UserBadge;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class BadgeDtoMapperTest {

    private final BadgeDtoMapper mapper = new BadgeDtoMapper();

    @Test
    void toResponse_mapsBadge() {
        Badge badge = Badge.builder().id(1L).code("collector").name("Coleccionista")
                .description("desc").criteriaType(BadgeCriteria.FAVORITES_COUNT).threshold(5).build();
        var response = mapper.toResponse(badge);
        assertThat(response.code()).isEqualTo("collector");
        assertThat(response.name()).isEqualTo("Coleccionista");
    }

    @Test
    void toResponse_mapsUserBadgeWithNestedBadge() {
        Badge badge = Badge.builder().id(1L).code("collector").name("Coleccionista")
                .description("desc").criteriaType(BadgeCriteria.FAVORITES_COUNT).threshold(5).build();
        LocalDateTime now = LocalDateTime.now();
        UserBadge userBadge = UserBadge.builder().id(1L).userId(1L).badge(badge).unlockedAt(now).build();

        var response = mapper.toResponse(userBadge);

        assertThat(response.badge().code()).isEqualTo("collector");
        assertThat(response.unlockedAt()).isEqualTo(now);
    }
}
