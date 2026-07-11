package com.pokedex.pokedex_api.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.model.ShareLink;
import com.pokedex.pokedex_api.core.model.ShareType;
import com.pokedex.pokedex_api.persistence.entity.relational.ShareLinkEntity;
import com.pokedex.pokedex_api.persistence.repository.relational.ShareLinkJpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShareLinkPersistenceAdapterTest {

    @Mock
    private ShareLinkJpaRepository repository;
    private ShareLinkPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ShareLinkPersistenceAdapter(repository);
    }

    @Test
    void findByToken_whenPresent_mapsToDomain() {
        when(repository.findByToken("abc")).thenReturn(Optional.of(
                ShareLinkEntity.builder().id(1L).token("abc").type(ShareType.POKEMON).refId(1L)
                        .ownerUserId(1L).createdAt(LocalDateTime.now()).build()));

        assertThat(adapter.findByToken("abc")).isPresent();
    }

    @Test
    void save_persistsLink() {
        when(repository.save(any())).thenReturn(
                ShareLinkEntity.builder().id(1L).token("abc").type(ShareType.TEAM).refId(2L)
                        .ownerUserId(1L).createdAt(LocalDateTime.now()).build());

        ShareLink result = adapter.save(ShareLink.builder().token("abc").type(ShareType.TEAM).refId(2L).ownerUserId(1L).build());

        assertThat(result.getToken()).isEqualTo("abc");
    }
}
