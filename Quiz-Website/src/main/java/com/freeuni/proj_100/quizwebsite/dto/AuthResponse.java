package com.freeuni.proj_100.quizwebsite.dto;

public record AuthResponse(
        Integer userId,
        String username,
        String token,
        boolean isAdmin
) { }
