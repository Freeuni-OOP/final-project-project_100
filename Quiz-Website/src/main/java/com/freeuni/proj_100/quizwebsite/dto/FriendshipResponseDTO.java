package com.freeuni.proj_100.quizwebsite.dto;

/**
 * A lightweight DTO used to transfer safe, non-sensitive friend data
 * to the frontend client.
 */
public record FriendshipResponseDTO(
        Integer userId,
        String username,
        String status
) {}