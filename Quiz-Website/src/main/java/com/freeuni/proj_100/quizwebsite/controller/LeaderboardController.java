package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.QuizAttemptDTO;
import com.freeuni.proj_100.quizwebsite.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for retrieving quiz leaderboards securely via DTOs.
 */
@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:5173")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * Retrieves the leaderboard for a specific quiz.
     */
    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<QuizAttemptDTO>> getLeaderboard(@PathVariable Long quizId) {
        List<QuizAttemptDTO> leaderboard = leaderboardService.getTopLeaderboard(quizId, 50);
        return ResponseEntity.ok(leaderboard);
    }
}