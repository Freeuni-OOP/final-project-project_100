package com.freeuni.proj_100.quizwebsite.dto;

public record AuthResponse(
        Long userId,
        String username,
        String token,
        boolean isAdmin
) { }
