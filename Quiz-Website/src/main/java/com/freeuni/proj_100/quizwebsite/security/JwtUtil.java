package com.freeuni.proj_100.quizwebsite.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility component for handling JSON Web Tokens.
 * Provides functionality to generate, parse, and validate JWTs used for 
 * stateless user authentication.
 */
@Component
public class JwtUtil {
    /**
     * Token expiration duration in milliseconds.
     */
    private final long expirationMs;

    /**
     * Cryptographic key used to sign and verify JWT tokens.
     */
    private final SecretKey secretKey;

    /**
     * Constructs a new JwtUtil with configuration properties injected from the application env.
     *
     * @param secret the raw secret string used to generate the HMAC signing key
     * @param expMs  the token lifetime/expiration duration in milliseconds
     */
    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expMs) {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expMs;
    }

    /**
     * Generates a signed JWT for a given username.
     *
     * @param username the username to embed as the subject of the token
     * @return a compact, URL-safe JWT string
     */
    public String generateToken(
            String username,
            Collection<? extends GrantedAuthority> authorities,
            int tokenVersion
    ) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + expirationMs);

        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("version", tokenVersion)
                .issuedAt(now)
                .expiration(expDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token the JWT token string
     * @return the username embedded in the token
     * @throws JwtException if the token cannot be parsed or verified
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the token version from JWT token.
     *
     */
    public int getVersion(String token) {
        return parseClaims(token).get("version", Integer.class);
    }

    /**
     * Extracts and parses the granted authorities (roles) embedded within the JWT token.
     *
     * @param token the signed JWT token string to extract claims from
     * @return a list of {@link GrantedAuthority} objects representing the user's roles, 
     * or an empty list if no roles are present or the claim is blank
     * @throws JwtException if the token signature is invalid or parsing fails
     */
    public List<GrantedAuthority> getAuthorities(String token) {
        String roles = parseClaims(token).get("roles", String.class);
        if (roles == null || roles.isBlank()) return List.of();

        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Validates whether a token is structurally sound, properly signed by this application, 
     * and not expired.
     *
     * @param token the JWT token string to validate
     * @return {@code true} if the token is valid and active, {@code false} if it is expired, 
     * malformed, or invalidly signed
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses the cryptographic claims payload from a signed JWT string.
     * This method verifies the signature using the configured secret key.
     *
     * @param token the JWT token string
     * @return the payload body (Claims) of the token
     * @throws JwtException if the signature verification fails or the token is otherwise invalid
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
