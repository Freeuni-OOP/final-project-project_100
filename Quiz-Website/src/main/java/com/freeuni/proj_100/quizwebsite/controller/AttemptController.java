package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.AttemptResultDto;
import com.freeuni.proj_100.quizwebsite.dto.AttemptSubmitRequest;
import com.freeuni.proj_100.quizwebsite.dto.CheckAnswerDto;
import com.freeuni.proj_100.quizwebsite.dto.CheckAnswerRequest;
import com.freeuni.proj_100.quizwebsite.service.AttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {
    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<AttemptResultDto> submitAttempt(
            @PathVariable Integer quizId,
            @RequestBody AttemptSubmitRequest request,
            @AuthenticationPrincipal String username) {

        AttemptResultDto result = attemptService.submitAttempt(
                quizId, request, username);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{quizId}/check-answer")
    public ResponseEntity<CheckAnswerDto> checkAnswer(
            @PathVariable Long quizId,
            @RequestBody CheckAnswerRequest request) {
        return ResponseEntity.ok(attemptService.checkAnswer(request));
    }
}
