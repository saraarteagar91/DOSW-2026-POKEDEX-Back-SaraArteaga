package com.pokedex.pokedex_api.core.model;

@lombok.Value
@lombok.Builder
public class Badge {
    Long id;
    String code;
    String name;
    String description;
    BadgeCriteria criteriaType;
    Integer threshold;
}
