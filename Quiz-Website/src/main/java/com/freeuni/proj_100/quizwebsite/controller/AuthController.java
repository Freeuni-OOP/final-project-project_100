package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.AuthResponse;
import com.freeuni.proj_100.quizwebsite.dto.LoginRequest;
import com.freeuni.proj_100.quizwebsite.dto.RegisterRequest;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller handling authentication requests such as registration, login, 
 * and user profile retrieval.
 * <p>
 * Exposed under the base path {@code /api/auth}. Registration and login routes 
 * are white-listed to be accessible by unauthenticated users.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * Core business service managing authentication mechanics and token issuing operations.
     */
    private final AuthService authService;

    /**
     * Constructs the controller with the required authentication service dependency.
     *
     * @param service the authentication service instance injected by Spring
     */
    public AuthController(AuthService service) {
        this.authService = service;
    }

    /**
     * Handles user registration.
     * Creates a new user record in the system database and generates an initial
     * authentication token.
     *
     * @param req a {@link RegisterRequest} DTO carrying identity data (username, email, password)
     * @return a {@link ResponseEntity} with HTTP status 201 (Created) housing the {@link AuthResponse} token details
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        AuthResponse resp = authService.register(req);
        return ResponseEntity.status(201).body(resp);
    }

    /**
     * Handles user authentication.
     * Verifies provided credentials against existing records to issue an active login token.
     *
     * @param req a {@link LoginRequest} DTO carrying verification credentials (username, password)
     * @return a {@link ResponseEntity} with HTTP status 200 (OK) housing the {@link AuthResponse} token details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    /**
     * Retrieves basic contextual profile details of the currently authenticated principal.
     * Uses Spring Security's execution thread parameters to inject active session references.
     *
     * @param username the authenticated user's username principal injected via {@link AuthenticationPrincipal}
     * @return a {@link ResponseEntity} containing a key-value structure of basic non-sensitive account data fields
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal String username) {
        User user = authService.getUserByUsername(username);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "isAdmin", user.isAdmin()
        ));
    }
}
