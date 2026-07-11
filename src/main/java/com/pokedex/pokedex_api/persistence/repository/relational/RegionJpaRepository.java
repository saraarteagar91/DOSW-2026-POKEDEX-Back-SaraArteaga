package com.pokedex.pokedex_api.persistence.repository.relational;

import com.pokedex.pokedex_api.persistence.entity.relational.RegionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionJpaRepository extends JpaRepository<RegionEntity, Long> {
    Optional<RegionEntity> findByNameIgnoreCase(String name);

    List<RegionEntity> findAllByOrderByNameAsc();
}
