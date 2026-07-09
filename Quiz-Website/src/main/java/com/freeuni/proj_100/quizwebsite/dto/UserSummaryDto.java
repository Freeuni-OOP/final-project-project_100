package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;

public record UserSummaryDto(
        Integer id,
        String username,
        String email,
        boolean isAdmin,
        LocalDateTime createdAt
) { }
