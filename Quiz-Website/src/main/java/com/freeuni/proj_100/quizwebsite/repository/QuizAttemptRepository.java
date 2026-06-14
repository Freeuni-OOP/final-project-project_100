package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for QuizAttempt entity.
 * Provides operations to query user histories and quiz leaderboards.
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    /**
     * Retrieves the history of non-practice quiz attempts for a specific user, ordered by most recent.
     *
     * @param userId The unique ID of the user.
     * @return A list of the user's past quiz attempts.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.userId = :userId AND a.isPractice = false ORDER BY a.takenAt DESC")
    List<QuizAttempt> getUserHistory(Long userId);

    /**
     * Retrieves the leaderboard for a specific quiz based on score (descending) and time taken (ascending).
     * Practice attempts are excluded.
     *
     * @param quizId The unique ID of the quiz.
     * @return A list of top quiz attempts for the leaderboard.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false ORDER BY a.score DESC, a.timeTakenSec ASC")
    List<QuizAttempt> getLeaderboard(int quizId);
}