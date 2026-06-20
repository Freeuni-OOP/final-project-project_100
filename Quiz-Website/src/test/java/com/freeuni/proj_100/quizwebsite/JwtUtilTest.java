package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(
                "test-secret-key-that-is-long-enough-32chars",
                86400000L
        );
    }

    @Test
    void generateTokenShouldReturnNonNull() {
        String token = jwtUtil.generateToken(
                "bob",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getUsernameReturnsCorrectUsername() {
        String token = jwtUtil.generateToken(
                "bob",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        assertEquals("bob", jwtUtil.getUsername(token));
    }

    @Test
    void getAuthoritiesReturnsCorrectRole() {
        String token = jwtUtil.generateToken(
                "bob",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        var authorities = jwtUtil.getAuthorities(token);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.get(0).getAuthority());
    }

    @Test
    void isTokenValidReturnsCorrectly() {
        String token = jwtUtil.generateToken(
                "alice",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        assertTrue(jwtUtil.isTokenValid(token));
        assertFalse(jwtUtil.isTokenValid("garbage-value"));
    }
}
