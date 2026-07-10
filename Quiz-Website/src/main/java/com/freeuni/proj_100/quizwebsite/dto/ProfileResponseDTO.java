package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for aggregating and securely transmitting complete user profiles.
 */
public record ProfileResponseDTO(
        Integer userId,
        String username,
        String email,
        boolean isAdmin,
        LocalDateTime joinedAt,
        String relation,
        List<QuizAttemptDTO> recentAttempts,
        List<String> achievements
) {}