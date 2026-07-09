package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;

public record AnnouncementDto(
        int id,
        String title,
        String content,
        String createdBy,
        LocalDateTime createdAt
) { }
