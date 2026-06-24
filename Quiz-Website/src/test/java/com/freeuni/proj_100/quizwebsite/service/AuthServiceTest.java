package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.AuthResponse;
import com.freeuni.proj_100.quizwebsite.dto.LoginRequest;
import com.freeuni.proj_100.quizwebsite.dto.RegisterRequest;
import com.freeuni.proj_100.quizwebsite.exception.AuthException;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepo;
    @Mock
    private PasswordEncoder passEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("bob");
        user.setEmail("bobert@gmail.com");
        user.setPasswordHash("hashed_password");
    }

    @Test
    void registerValidRequestTest() {
        when(userRepo.existsByUsername("bob")).thenReturn(false);
        when(userRepo.existsByEmail("bobert@gmail.com")).thenReturn(false);
        when(passEncoder.encode("secret123")).thenReturn("hashed_password");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(eq("bob"), any(), 0)).thenReturn("mock.jwt.token");

        RegisterRequest request = new RegisterRequest("bob", "bobert@gmail.com", "secret123");
        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.token());
        assertEquals("bob", response.username());
    }
    
    @Test
    void registerInvalidRequestUsernameTaken() {
        when(userRepo.existsByUsername("bob")).thenReturn(true);
        RegisterRequest request = new RegisterRequest("bob", "bobert@gmail.com", "secret123");

        AuthException ex = assertThrows(AuthException.class, () -> authService.register(request));
        assertTrue(ex.getMessage().contains("Username already taken"));

        verify(userRepo, never()).save(any());
    }

    @Test
    void registerInvalidRequestEmailTaken() {
        when(userRepo.existsByUsername("bob")).thenReturn(false);
        when(userRepo.existsByEmail("bobert@gmail.com")).thenReturn(true);

        RegisterRequest request = new RegisterRequest("bob", "bobert@gmail.com", "secret123");
        AuthException ex = assertThrows(AuthException.class, () -> authService.register(request));
        assertTrue(ex.getMessage().contains("Email already registered"));

        verify(userRepo, never()).save(any());
    }

    @Test
    void loginValidRequestTest() {
        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(user));
        when(passEncoder.matches("secret123", "hashed_password")).thenReturn(true);
        when(jwtUtil.generateToken(eq("bob"), any(), 0)).thenReturn("mock.jwt.token");

        LoginRequest request = new LoginRequest("bob", "secret123");
        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.token());
        assertEquals("bob", response.username());
    }

    @Test
    void loginInvalidRequestUserNotFound() {
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest("unknown", "secret123");

        AuthException ex = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("Invalid username or password", ex.getMessage());
    }

    @Test
    void loginInvalidRequestWrongPassword() {
        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(user));
        when(passEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);

        LoginRequest request = new LoginRequest("bob", "wrongpassword");

        AuthException ex = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("Invalid username or password", ex.getMessage());
    }

    @Test
    void loginBadUsernameAndBadPasswordReturnSameError() {
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());
        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(user));
        when(passEncoder.matches(any(), any())).thenReturn(false);

        AuthException noUser = assertThrows(AuthException.class,
                () -> authService.login(new LoginRequest("unknown", "any")));
        AuthException wrongPass = assertThrows(AuthException.class,
                () -> authService.login(new LoginRequest("bob", "wrong")));

        assertEquals(noUser.getMessage(), wrongPass.getMessage());
    }
}
