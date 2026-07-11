package com.pokedex.pokedex_api.controller.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pokedex.pokedex_api.core.exception.BusinessException;
import com.pokedex.pokedex_api.core.exception.DuplicateResourceException;
import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import com.pokedex.pokedex_api.core.exception.InvalidCredentialsException;
import com.pokedex.pokedex_api.core.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/v1/pokemon/1");
    }

    @Test
    void handleNotFound_returns404() {
        var response = handler.handleNotFound(new ResourceNotFoundException("Pokemon", "id", 1L), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().errorCode()).isEqualTo("NOT_FOUND");
    }

    @Test
    void handleDuplicate_returns409() {
        var response = handler.handleDuplicate(new DuplicateResourceException("Pokemon", "nationalNumber", 1), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleForbiddenOperation_returns409() {
        var response = handler.handleForbiddenOperation(new ForbiddenOperationException("no permitido"), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleInvalidCredentials_returns401() {
        var response = handler.handleInvalidCredentials(new InvalidCredentialsException(), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleBusiness_returns400() {
        var response = handler.handleBusiness(new BusinessException("mensaje", "CODE"), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleAccessDenied_returns403() {
        var response = handler.handleAccessDenied(new AccessDeniedException("denegado"), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void handleAuthentication_returns401() {
        var response = handler.handleAuthentication(new AuthenticationException("no autenticado") { }, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleUnexpected_returns500() {
        var response = handler.handleUnexpected(new RuntimeException("boom"), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
