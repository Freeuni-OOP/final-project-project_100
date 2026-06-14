package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents an achievement earned by a user.
 * Maps to the "user_achievements" table in the database.
 */
@Entity
@Table(name = "user_achievements")
public class UserAchievement {

    /** Auto-incremented primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the user who earned the achievement. */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** The specific type or name of the achievement earned. */
    @Column(name = "achievement_type", nullable = false)
    private String achievementType;

    /** Timestamp of when the achievement was earned. */
    @Column(name = "earned_at", nullable = false)
    private LocalDateTime earnedAt = LocalDateTime.now();

    public UserAchievement() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAchievementType() { return achievementType; }
    public void setAchievementType(String type) { this.achievementType = type; }
    public LocalDateTime getEarnedAt() { return earnedAt; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }
}