package com.freeuni.proj_100.quizwebsite.model;

/**
 * Represents the restricted states of a friendship connection.
 * Maps directly to the MySQL ENUM('pending', 'accepted', 'rejected').
 */
public enum FriendshipStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}