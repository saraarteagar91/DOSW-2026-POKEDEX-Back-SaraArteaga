package com.pokedex.pokedex_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Separado de {@link SecurityConfig} a propósito: AuthServiceImpl necesita el {@link PasswordEncoder},
 * y SecurityConfig depende (vía OAuth2SuccessHandler) de AuthService — definirlo aquí evita el ciclo.
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
