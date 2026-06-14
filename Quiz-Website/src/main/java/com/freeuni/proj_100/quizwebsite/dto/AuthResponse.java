package com.freeuni.proj_100.quizwebsite.dto;

public record AuthResponse(
        Long UserId,
        String username,
        String token
) { }
