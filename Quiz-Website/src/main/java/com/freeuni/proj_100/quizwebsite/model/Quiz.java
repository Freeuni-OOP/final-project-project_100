package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a quiz in the system.
 * Maps to the "quizzes" table in the database.
 */
@Entity
@Table(name = "quizzes")
public class Quiz {

    /** Auto-incremented primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** ID of the user who created this quiz. */
    @Column(name = "creator_id")
    private int creatorId;

    /** Title of the quiz. */
    @Column(nullable = false)
    private String title;

    /** Optional description of the quiz. */
    @Column
    private String description;

    /** Whether questions are presented in random order. */
    @Column(name = "is_randomized")
    private boolean isRandomized;

    /** Whether all questions appear on a single page. */
    @Column(name = "is_single_page")
    private boolean isSinglePage;

    /** Whether the user gets immediate feedback after each answer. */
    @Column(name = "immediate_feedback")
    private boolean immediateFeedback;

    /** Whether the quiz can be taken in practice mode. */
    @Column(name = "allow_practice")
    private boolean allowPractice;

    /** Timestamp of when the quiz was created. */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Quiz() {}

    public int getId() { return id; }
    public int getCreatorId() { return creatorId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isRandomized() { return isRandomized; }
    public boolean isSinglePage() { return isSinglePage; }
    public boolean isImmediateFeedback() { return immediateFeedback; }
    public boolean isAllowPractice() { return allowPractice; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setCreatorId(int creatorId) { this.creatorId = creatorId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRandomized(boolean randomized) { isRandomized = randomized; }
    public void setSinglePage(boolean singlePage) { isSinglePage = singlePage; }
    public void setImmediateFeedback(boolean immediateFeedback) { this.immediateFeedback = immediateFeedback; }
    public void setAllowPractice(boolean allowPractice) { this.allowPractice = allowPractice; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}