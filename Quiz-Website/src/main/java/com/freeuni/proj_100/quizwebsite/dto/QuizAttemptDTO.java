package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for safely transmitting quiz attempt data to the frontend.
 */
public record QuizAttemptDTO(
        Long id,
        String username,
        Long quizId,
        int score,
        int timeTakenSec,
        LocalDateTime takenAt
) {}