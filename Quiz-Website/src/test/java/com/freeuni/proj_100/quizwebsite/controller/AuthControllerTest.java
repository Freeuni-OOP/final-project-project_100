package com.freeuni.proj_100.quizwebsite.controller;


import com.freeuni.proj_100.quizwebsite.dto.AuthResponse;
import com.freeuni.proj_100.quizwebsite.dto.LoginRequest;
import com.freeuni.proj_100.quizwebsite.dto.RegisterRequest;
import com.freeuni.proj_100.quizwebsite.exception.AuthException;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.security.JwtAuthFilter;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import com.freeuni.proj_100.quizwebsite.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @InjectMocks
    private AuthController authController;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void registerShouldReturn201WhenValid() {
        RegisterRequest req = new RegisterRequest("alice", "alice@example.com", "secret123");
        AuthResponse mockResp = new AuthResponse(1L, "alice", "mock.token");
        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResp);

        ResponseEntity<AuthResponse> response = authController.register(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock.token", response.getBody().token());
        assertEquals("alice", response.getBody().username());
        assertEquals(1L, response.getBody().userId());
    }

    @Test
    void registerShouldThrowExceptionWhenUsernameTaken() {
        RegisterRequest req = new RegisterRequest("alice", "alice@example.com", "secret123");
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new AuthException("Username already taken: alice"));

        AuthException exception = assertThrows(AuthException.class, () -> {
            authController.register(req);
        });
        assertEquals("Username already taken: alice", exception.getMessage());
    }

    @Test
    void registerShouldReturn400WhenFieldsInvalid() {
        RegisterRequest req = new RegisterRequest("", "not-an-email", "short");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Expected validation errors, but found none.");
    }

    @Test
    void loginShouldReturn200WhenValid() {
        LoginRequest req = new LoginRequest("alice", "secret123");
        AuthResponse mockResponse = new AuthResponse(1L, "alice", "mock.token");
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.login(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock.token", response.getBody().token());
        assertEquals("alice", response.getBody().username());
    }

    @Test
    void loginShouldThrowExceptionWhenBadCredentials() {
        LoginRequest req = new LoginRequest("alice", "wrongpassword");
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new AuthException("Invalid username or password"));

        AuthException exception = assertThrows(AuthException.class, () -> {
            authController.login(req);
        });
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void loginShouldNotRevealWhichFieldWasWrong() {
        LoginRequest badUsernameReq = new LoginRequest("unknown", "secret123");
        LoginRequest badPasswordReq = new LoginRequest("alice", "wrongpassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new AuthException("Invalid username or password"));

        AuthException exception1 = assertThrows(AuthException.class, () -> authController.login(badUsernameReq));
        AuthException exception2 = assertThrows(AuthException.class, () -> authController.login(badPasswordReq));

        assertEquals(exception1.getMessage(), exception2.getMessage());
    }
    
    @Test
    void meShouldReturnUserDetailsWhenValid() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("alice");
        mockUser.setEmail("alice@example.com");
        
        when(authService.getUserByUsername("alice")).thenReturn(mockUser);

        ResponseEntity<Map<String, Object>> response = authController.me("alice");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().get("id"));
        assertEquals("alice", response.getBody().get("username"));
        assertEquals("alice@example.com", response.getBody().get("email"));
    }

    @Test
    void meShouldThrowExceptionWhenUserNotFound() {
        when(authService.getUserByUsername("unknown"))
                .thenThrow(new AuthException("User not found"));

        AuthException exception = assertThrows(AuthException.class, () -> {
            authController.me("unknown");
        });
        
        assertEquals("User not found", exception.getMessage());
    }
}