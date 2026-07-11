package com.pokedex.pokedex_api.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Beans generales de la aplicación. */
@Configuration
public class AppConfig {

    /** Inyectado en vez de {@code LocalDate.now()} directo, para poder probar RF-19 (Pokémon del día). */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
