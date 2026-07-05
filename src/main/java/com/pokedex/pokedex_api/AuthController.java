package com.pokedex.pokedex_api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest datos) {
        if (datos.correo().equals("sara@pokebloom.com") && datos.password().equals("1234")) {
            String token = tokenService.crearToken(datos.correo());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401)
                             .body(Map.of("error", "Correo o contraseña incorrectos"));
    }
}