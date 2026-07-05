package com.pokedex.pokedex_api;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {
    @Value("${app.jwt.secret}")
    private String claveSecreta;
    @Value("${app.jwt.expiration-ms}")
    private long duracionMs;
    private SecretKey clave() {
        return Keys.hmacShaKeyFor(claveSecreta.getBytes(StandardCharsets.UTF_8));
    }
    public String crearToken(String correo) {
        return Jwts.builder()
                .subject(correo)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duracionMs))
                .signWith(clave())
                .compact();
    }
    public String leerCorreo(String token) {
        return Jwts.parser()
                   .verifyWith(clave())
                   .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean esValido(String token) {
        try {
            Jwts.parser().verifyWith(clave()).build()
                                             .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}