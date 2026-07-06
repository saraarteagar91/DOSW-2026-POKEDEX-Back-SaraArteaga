package com.pokedex.pokedex_api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/perfil")
public class PerfilController {
    @GetMapping
    public ResponseEntity<String> miPerfil(Authentication auth) {
        String correo = auth.getName();
        return ResponseEntity.ok("Bienvenida a PokeBloom, " + correo + ". Estás en una ruta protegida.");
    }
}