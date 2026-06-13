package com.freeuni.proj_100.quizwebsite.dto;

public class AuthResponse {
    private Long userId;
    private String username;
    private String token;

    public AuthResponse() {}

    public AuthResponse(Long id, String username, String token) {
        this.userId = id;
        this.username = username;
        this.token = token;
    }

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getToken() { return token; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setToken(String token) { this.token = token; }
}
