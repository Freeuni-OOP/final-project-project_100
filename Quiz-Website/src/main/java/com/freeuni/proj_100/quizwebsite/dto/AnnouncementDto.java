package com.freeuni.proj_100.quizwebsite.dto;

import java.time.LocalDateTime;

public record AnnouncementDto(
        long id,
        String title,
        String content,
        String createdBy,
        LocalDateTime createdAt
) { }
