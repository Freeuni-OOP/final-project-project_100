package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.AuthResponse;
import com.freeuni.proj_100.quizwebsite.dto.LoginRequest;
import com.freeuni.proj_100.quizwebsite.dto.RegisterRequest;
import com.freeuni.proj_100.quizwebsite.exception.AuthException;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class handling core business logic for user authentication lifecycle processes.
 *
 */
@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Constructs a new {@code AuthService} with all necessary functional components.
     *
     * @param userRepo    the data access abstraction layer for user records
     * @param passEncoder the hashing provider used to encode text passwords securely
     * @param jwtUtil     the token manager used to sign and handle session details
     */
    public AuthService(UserRepository userRepo,
                       PasswordEncoder passEncoder,
                       JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Validates and registers a new user into the system database.
     * Checks identity uniqueness requirements across both usernames and emails 
     * before persisting encrypted data attributes.
     *
     * @param req the user details packet carrying registration credentials
     * @return an {@link AuthResponse} containing the new account identifier, profile username, and signed session token
     * @throws AuthException if the requested username or email has already been taken in the database
     */
    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            throw new AuthException("Username already taken: " + req.username());
        }

        if (userRepo.existsByEmail(req.email())) {
            throw new AuthException("Email already registered: " + req.email());
        }

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));

        User savedUser = userRepo.save(user);
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getAuthorities());

        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), token);
    }

    /**
     * Authenticates an existing user record matching incoming credential arguments.
     * Validates password arguments safely against secured database hashes.
     *
     * @param req the user challenge credentials containing the identifier details
     * @return an {@link AuthResponse} containing the active account identifier, profile username, and signed session token
     * @throws AuthException if the provided username does not exist or password hashes do not match
     */
    public AuthResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new AuthException("Invalid username or password"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new AuthException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getAuthorities());

        return new AuthResponse(user.getId(), user.getUsername(), token);
    }

    /**
     * Helper function for getting the User by username
     *
     * @param username unique username
     * @return User object with the specified unique username
     */
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));
    }
}
