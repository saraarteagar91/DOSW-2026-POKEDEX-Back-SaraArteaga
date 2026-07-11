package com.pokedex.pokedex_api.persistence.entity.relational;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entidad JPA del catálogo. Vive solo en persistence; core/model.Pokemon no conoce JPA (§5.4 doc arquitectura).
 *
 * <p>Se agrega {@code @Setter} (desviación deliberada del ejemplo de solo-getters del documento base):
 * RF-12 exige edición real del catálogo, y mutar en el sitio la entidad ya gestionada por Hibernate es más
 * simple y correcto que reconstruirla por completo en cada actualización. Los setters solo se usan desde
 * {@code PokemonPersistenceAdapter}, dentro de la capa persistence.</p>
 */
@Entity
@Table(name = "pokemon", indexes = {@Index(name = "idx_pokemon_number", columnList = "national_number")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_number", nullable = false, unique = true)
    private Integer nationalNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Integer generation;

    @Column(name = "has_mega", nullable = false)
    private Boolean hasMega;

    @Column(name = "evolution_stage", nullable = false)
    private Integer evolutionStage;

    @Column(name = "evolves_from_id")
    private Long evolvesFromId;

    @Column(length = 30)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    // Set (no List) a propósito: Hibernate no permite fetch-join simultáneo de más de una colección
    // tipo "bag" (MultipleBagFetchException) — con Set, @EntityGraph puede traer types+abilities+moves
    // en una sola consulta sin N+1 (doc §11.2).
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pokemon_type",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    @Builder.Default
    private Set<TypeEntity> types = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pokemon_ability",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "ability_id"))
    @Builder.Default
    private Set<AbilityEntity> abilities = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pokemon_move",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "move_id"))
    @Builder.Default
    private Set<MoveEntity> moves = new LinkedHashSet<>();

    @OneToOne(mappedBy = "pokemon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PokemonStatsEntity stats;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
