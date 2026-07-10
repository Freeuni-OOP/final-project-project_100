package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for QuizAttempt entity.
 * Provides operations to query user histories and paginated quiz leaderboards.
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {

    /**
     * Retrieves the history of non-practice quiz attempts for a specific user across all quizzes.
     */
    List<QuizAttempt> findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(Integer userId);

    /**
     * Highest performers of all time.
     * This serves as the primary leaderboard query.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false ORDER BY a.score DESC, a.timeTakenSec ASC")
    List<QuizAttempt> getTopPerformersAllTime(Integer quizId, Pageable pageable);

    /**
     * Retrieves the history of non-practice quiz attempts for a specific user, ordered by most recent.
     */
    List<QuizAttempt> findByQuizIdAndIsPracticeFalseOrderByScoreDescTimeTakenSecDesc(Integer quizId, Pageable pageable);

    /**
     * Retrieves the leaderboard for a specific quiz based on score (descending) and time taken (ascending).
     * Now accepts a Pageable object to prevent memory overload.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false ORDER BY a.takenAt DESC")
    List<QuizAttempt> getRecentTestTakers(Integer quizId, Pageable pageable);

    /**
     * User's past performance on this specific quiz.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.user.username = :username AND a.isPractice = false ORDER BY a.takenAt DESC")
    List<QuizAttempt> getUserPastPerformance(Integer quizId, String username);

    /**
     * Deletes the quiz attempt by id
     */
    void deleteByQuizId(Integer quizId);
}