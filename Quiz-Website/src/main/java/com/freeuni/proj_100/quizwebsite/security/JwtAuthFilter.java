package com.freeuni.proj_100.quizwebsite.security;

import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Security filter that intercepts incoming HTTP requests to validate JWT access tokens.
 * If a valid token is found in the {@code Authorization} header, the user authentication details
 * are loaded and established within the Spring Security security context.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    /**
     * Constructs a new {@code JwtAuthFilter} with its required helper components.
     *
     * @param jwtUtil            the JWT utility instance injected by Spring
     */
    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository userRepo) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    /**
     * Processes individual incoming requests to check for a bearer JWT token in the 
     * HTTP {@code Authorization} header. If valid, updates the {@link SecurityContextHolder}.
     *
     * @param req          the incoming servlet request
     * @param resp         the outgoing servlet response
     * @param filterChains the system filter chain execution flow control object
     * @throws ServletException if an error occurs during filter processing
     * @throws IOException      if an input or output error occurs during servlet execution
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse resp,
            FilterChain filterChains)
            throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChains.doFilter(req, resp);
            return;
        }

        // token starts after 'Bearer '
        String token = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            filterChains.doFilter(req, resp);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtil.getUsername(token);
            int tokenVersion = jwtUtil.getVersion(token);

            Optional<Integer> currVersion = userRepo.findTokenVersionByUsername(username);
            if (currVersion.isEmpty() || currVersion.get() != tokenVersion) {
                filterChains.doFilter(req, resp);
                return;
            }

            List<GrantedAuthority> authorities = jwtUtil.getAuthorities(token);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(req)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChains.doFilter(req, resp);
    }

    /**
     * Specifies request patterns that skip filter checks completely. 
     * Authentication routes under {@code /api/auth/} (e.g., login, registration) are skipped
     * because clients do not possess tokens prior to authenticating.
     *
     * @param req the current HTTP request context path evaluated
     * @return {@code true} if the filter should bypass processing, {@code false} otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        return req.getServletPath().startsWith("/api/auth/");
    }
}
