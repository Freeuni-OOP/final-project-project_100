package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a user's single attempt at taking a quiz.
 * Maps to the "quiz_attempts" table in the database.
 */
@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    /** Auto-incremented primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the user who took the quiz. */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** ID of the quiz that was taken. */
    @Column(name = "quiz_id", nullable = false)
    private int quizId;

    /** The final score achieved on the attempt. */
    @Column(nullable = false)
    private int score;

    /** Total time taken to complete the quiz in seconds. */
    @Column(name = "time_taken_sec", nullable = false)
    private int timeTakenSec;

    /** Indicates whether the attempt was taken in practice mode (not scored for leaderboards). */
    @Column(name = "is_practice", nullable = false)
    private boolean isPractice;

    /** Timestamp of when the attempt was submitted. */
    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt = LocalDateTime.now();

    public QuizAttempt() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTimeTakenSec() { return timeTakenSec; }
    public void setTimeTakenSec(int timeTakenSec) { this.timeTakenSec = timeTakenSec; }
    public boolean isPractice() { return isPractice; }
    public void setPractice(boolean practice) { isPractice = practice; }
    public LocalDateTime getTakenAt() { return takenAt; }
    public void setTakenAt(LocalDateTime takenAt) { this.takenAt = takenAt; }
}