package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;

public record UserSummaryDto(
        Long id,
        String username,
        String email,
        boolean isAdmin,
        LocalDateTime createdAt
) { }
