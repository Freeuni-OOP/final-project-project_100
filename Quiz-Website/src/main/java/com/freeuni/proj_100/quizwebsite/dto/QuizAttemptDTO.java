package com.freeuni.proj_100.quizwebsite.dto;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
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
) {
    /**
     * Custom constructor to easily map a QuizAttempt database entity into this DTO.
     * This makes `.map(QuizAttemptDTO::new)` work perfectly in the service layer!
     */
    public QuizAttemptDTO(QuizAttempt attempt) {
        this(
                attempt.getId(),
                attempt.getUser().getUsername(), // Safely extracts the string username from the User entity
                attempt.getQuizId(),
                attempt.getScore(),
                attempt.getTimeTakenSec(),
                attempt.getTakenAt()
        );
    }
}