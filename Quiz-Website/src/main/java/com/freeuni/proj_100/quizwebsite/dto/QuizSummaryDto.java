package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;

public record QuizSummaryDto(
        Integer id,
        String title,
        String description,
        String createdBy,
        LocalDateTime createdAt,
        int questionCount
) {}
