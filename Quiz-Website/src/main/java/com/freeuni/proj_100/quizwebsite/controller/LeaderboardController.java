package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for retrieving quiz leaderboards.
 */
@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaderboardController {

    private final QuizAttemptRepository attemptRepository;

    /**
     * Constructs the LeaderboardController.
     *
     * @param attemptRepository Repository for fetching quiz attempts.
     */
    public LeaderboardController(QuizAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    /**
     * Retrieves the leaderboard for a specific quiz.
     *
     * @param quizId The unique ID of the quiz.
     * @return A ResponseEntity containing a list of top quiz attempts.
     */
    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<QuizAttempt>> getLeaderboard(@PathVariable int quizId) {
        List<QuizAttempt> leaderboard = attemptRepository.getLeaderboard(quizId);
        return ResponseEntity.ok(leaderboard);
    }
}