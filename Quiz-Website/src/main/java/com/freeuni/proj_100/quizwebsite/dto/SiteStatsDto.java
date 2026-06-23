package com.freeuni.proj_100.quizwebsite.dto;

public record SiteStatsDto(
        long totalUsers,
        long totalQuizzes,
        long totalAttempts,
        long newUsersToday
) { }
