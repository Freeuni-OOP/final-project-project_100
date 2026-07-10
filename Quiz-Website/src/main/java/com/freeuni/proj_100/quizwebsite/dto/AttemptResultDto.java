package com.freeuni.proj_100.quizwebsite.dto;

import java.util.List;

public record AttemptResultDto(
        int score,
        int totalQuestions,
        int timeTakenSec,
        List<QuestionResultDto> results
) {
    public record QuestionResultDto(
            Integer questionId,
            boolean correct,
            String correctAnswer,
            String userAnswer
    ) {}
}