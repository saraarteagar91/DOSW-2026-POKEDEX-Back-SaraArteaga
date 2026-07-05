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
    private SecretKey obtenerClave() {
        return Keys.hmacShaKeyFor(claveSecreta.getBytes(StandardCharsets.UTF_8));
    }
    public String generarToken(String correo) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + duracionMs);
        return Jwts.builder()
                .subject(correo)
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(obtenerClave())
                .compact();
    }
    public String obtenerCorreo(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean esValido(String token) {
        try {
                Jwts.parser()
                    .verifyWith(obtenerClave()).build()
                                               .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}