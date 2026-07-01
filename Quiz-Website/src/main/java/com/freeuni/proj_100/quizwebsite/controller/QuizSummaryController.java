package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.QuizSummaryDTO;
import com.freeuni.proj_100.quizwebsite.service.QuizSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:5173")
public class QuizSummaryController {

    // 1. Inject your new service here
    private final QuizSummaryService quizSummaryService;

    public QuizSummaryController(QuizSummaryService quizSummaryService) {
        this.quizSummaryService = quizSummaryService;
    }

    @GetMapping("/{quizId}/summary")
    public ResponseEntity<QuizSummaryDTO> getQuizSummary(@PathVariable Long quizId, Principal principal) {
        // 2. Call your new service
        return ResponseEntity.ok(quizSummaryService.getQuizSummary(quizId, principal.getName()));
    }
}