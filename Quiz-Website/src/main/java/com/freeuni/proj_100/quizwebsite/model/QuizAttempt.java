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
    private Integer id;

    /** The user entity who took the quiz (Many-to-One relationship). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** ID of the quiz that was taken. */
    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;

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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTimeTakenSec() { return timeTakenSec; }
    public void setTimeTakenSec(int timeTakenSec) { this.timeTakenSec = timeTakenSec; }

    public boolean isPractice() { return isPractice; }
    public void setPractice(boolean practice) { this.isPractice = practice; }

    public LocalDateTime getTakenAt() { return takenAt; }
    public void setTakenAt(LocalDateTime takenAt) { this.takenAt = takenAt; }
}