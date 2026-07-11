package com.pokedex.pokedex_api.security;

import com.pokedex.pokedex_api.core.model.User;
import com.pokedex.pokedex_api.core.service.interfaces.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/** RF-06: tras el callback de Google, crea/recupera la cuenta (RN-03) y emite un JWT propio (doc §3, capa security). */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtService jwtService;

    @Value("${app.oauth2.success-redirect-uri:http://localhost:5173/oauth2/callback}")
    private String successRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        User user = authService.findOrCreateGoogleUser(email, name, picture);
        String token = jwtService.generateToken(new AuthenticatedUser(user));

        String redirectUrl = UriComponentsBuilder.fromUriString(successRedirectUri)
                .queryParam("token", token)
                .build().toUriString();
        response.sendRedirect(redirectUrl);
    }
}
